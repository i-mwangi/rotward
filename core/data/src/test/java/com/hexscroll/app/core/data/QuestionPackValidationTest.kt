package com.hexscroll.app.core.data

import com.hexscroll.app.core.model.LearningTopic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

@Serializable
private data class QuestionJson(
    val id: String,
    val topic: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
)

class QuestionPackValidationTest {

    private val json = Json { ignoreUnknownKeys = true }

    private fun loadPack(assetFileName: String): List<QuestionJson> {
        val stream = javaClass.classLoader!!.getResourceAsStream("questions/$assetFileName")
            ?: error("Asset not found: questions/$assetFileName")
        val raw = stream.bufferedReader().readText()
        return json.decodeFromString(raw)
    }

    @Test
    fun `all packs parse without error and contain at least 20 questions`() {
        LearningTopic.entries.forEach { topic ->
            val questions = loadPack(topic.assetFileName)
            assertTrue(
                "Topic ${topic.id} must have at least 20 questions, found ${questions.size}",
                questions.size >= 20,
            )
        }
    }

    @Test
    fun `all question IDs are unique across all packs`() {
        val allIds = mutableListOf<String>()
        LearningTopic.entries.forEach { topic ->
            allIds += loadPack(topic.assetFileName).map { it.id }
        }
        val duplicates = allIds.groupBy { it }.filter { it.value.size > 1 }.keys
        assertTrue("Duplicate IDs found: $duplicates", duplicates.isEmpty())
    }

    @Test
    fun `every question has exactly 4 options`() {
        LearningTopic.entries.forEach { topic ->
            loadPack(topic.assetFileName).forEach { q ->
                assertEquals("Question ${q.id} must have 4 options", 4, q.options.size)
            }
        }
    }

    @Test
    fun `every question has valid correctIndex in 0-3`() {
        LearningTopic.entries.forEach { topic ->
            loadPack(topic.assetFileName).forEach { q ->
                assertTrue(
                    "Question ${q.id} has invalid correctIndex ${q.correctIndex}",
                    q.correctIndex in 0..3,
                )
            }
        }
    }

    @Test
    fun `every question has non-blank prompt and explanation`() {
        LearningTopic.entries.forEach { topic ->
            loadPack(topic.assetFileName).forEach { q ->
                assertFalse("Question ${q.id} has blank prompt", q.prompt.isBlank())
                assertFalse("Question ${q.id} has blank explanation", q.explanation.isBlank())
            }
        }
    }

    @Test
    fun `every question topic field matches the file topic ID`() {
        LearningTopic.entries.forEach { topic ->
            loadPack(topic.assetFileName).forEach { q ->
                assertEquals(
                    "Question ${q.id} topic mismatch: expected ${topic.id}, got ${q.topic}",
                    topic.id,
                    q.topic,
                )
            }
        }
    }

    @Test
    fun `every question topic ID maps to a valid LearningTopic enum`() {
        LearningTopic.entries.forEach { topic ->
            loadPack(topic.assetFileName).forEach { q ->
                assertNotNull(
                    "Question ${q.id} has unknown topic ID ${q.topic}",
                    LearningTopic.fromId(q.topic),
                )
            }
        }
    }
}
