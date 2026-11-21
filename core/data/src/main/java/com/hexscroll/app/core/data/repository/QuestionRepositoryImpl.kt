package com.hexscroll.app.core.data.repository

import com.hexscroll.app.core.data.database.QuestionBankSeeder
import com.hexscroll.app.core.data.database.dao.QuestionDao
import com.hexscroll.app.core.model.LearningTopic
import com.hexscroll.app.core.model.Question
import com.hexscroll.app.core.repository.QuestionRepository
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val seeder: QuestionBankSeeder,
) : QuestionRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun ensureSeeded() { seeder.ensureSeeded() }

    override suspend fun getNextQuestion(topics: Set<LearningTopic>): Question? {
        if (topics.isEmpty()) return null
        val topicIds = topics.map { it.id }
        val entity = questionDao.getNextQuestionForTopics(topicIds) ?: return null
        return try {
            val options = json.decodeFromString(ListSerializer(String.serializer()), entity.optionsJson)
            val topic = LearningTopic.fromId(entity.topic) ?: return null
            Question(
                id = entity.id,
                topic = topic,
                prompt = entity.prompt,
                options = options,
                correctIndex = entity.correctIndex,
                explanation = entity.explanation,
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to decode question ${entity.id}")
            null
        }
    }

    override suspend fun recordAnswer(questionId: String, isCorrect: Boolean, nowMillis: Long) {
        questionDao.recordAnswer(questionId, isCorrect, nowMillis)
    }
}
