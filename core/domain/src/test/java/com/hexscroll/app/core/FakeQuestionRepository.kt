package com.hexscroll.app.core

import com.hexscroll.app.core.model.LearningTopic
import com.hexscroll.app.core.model.Question
import com.hexscroll.app.core.repository.QuestionRepository

class FakeQuestionRepository : QuestionRepository {
    var nextQuestion: Question? = Question(
        id = "test_001",
        topic = LearningTopic.PROGRAMMING,
        prompt = "Test question?",
        options = listOf("A", "B", "C", "D"),
        correctIndex = 0,
        explanation = "Test explanation",
    )
    var seededCount = 0
    val answers = mutableListOf<Triple<String, Boolean, Long>>()

    override suspend fun ensureSeeded() { seededCount++ }

    override suspend fun getNextQuestion(topics: Set<LearningTopic>): Question? = nextQuestion

    override suspend fun recordAnswer(questionId: String, isCorrect: Boolean, nowMillis: Long) {
        answers += Triple(questionId, isCorrect, nowMillis)
    }
}
