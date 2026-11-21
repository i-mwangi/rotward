package com.hexscroll.app.core.repository

import com.hexscroll.app.core.model.LearningTopic
import com.hexscroll.app.core.model.Question

interface QuestionRepository {
    suspend fun ensureSeeded()
    suspend fun getNextQuestion(topics: Set<LearningTopic>): Question?
    suspend fun recordAnswer(questionId: String, isCorrect: Boolean, nowMillis: Long = System.currentTimeMillis())
}
