package com.hexscroll.app.core.data.database

import android.content.Context
import com.hexscroll.app.core.data.database.dao.QuestionDao
import com.hexscroll.app.core.data.database.model.QuestionEntity
import com.hexscroll.app.core.model.LearningTopic
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
private data class QuestionJson(
    val id: String,
    val topic: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
)

@Singleton
class QuestionBankSeeder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val questionDao: QuestionDao,
) {
    private val mutex = Mutex()
    private val json = Json { ignoreUnknownKeys = true }
    private var seeded = false

    suspend fun ensureSeeded() = mutex.withLock {
        if (seeded) return@withLock
        val count = questionDao.count()
        if (count > 0) {
            seeded = true
            return@withLock
        }

        withContext(Dispatchers.IO) {
            LearningTopic.entries.forEach { topic ->
                try {
                    val raw = context.assets.open("questions/${topic.assetFileName}")
                        .bufferedReader()
                        .readText()
                    val questions = json.decodeFromString<List<QuestionJson>>(raw)
                    val entities = questions
                        .filter { q ->
                            q.options.size == 4 &&
                                q.correctIndex in 0..3 &&
                                q.topic == topic.id &&
                                q.prompt.isNotBlank() &&
                                q.explanation.isNotBlank()
                        }
                        .map { q ->
                            QuestionEntity(
                                id = q.id,
                                topic = q.topic,
                                prompt = q.prompt,
                                optionsJson = json.encodeToString(ListSerializer(String.serializer()), q.options),
                                correctIndex = q.correctIndex,
                                explanation = q.explanation,
                            )
                        }
                    questionDao.insertAll(entities)
                    Timber.d("Seeded ${entities.size} questions for ${topic.id}")
                } catch (e: Exception) {
                    Timber.e(e, "Failed to seed questions for ${topic.id}")
                }
            }
        }
        seeded = true
    }
}
