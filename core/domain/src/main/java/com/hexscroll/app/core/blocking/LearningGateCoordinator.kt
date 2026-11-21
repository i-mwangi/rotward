package com.hexscroll.app.core.blocking

import com.hexscroll.app.core.model.LearningGateDecision

interface LearningGateCoordinator {
    suspend fun evaluateBlock(nowMillis: Long = System.currentTimeMillis()): LearningGateDecision
    suspend fun onQuizPassed(questionId: String, nowMillis: Long = System.currentTimeMillis()): Long
    suspend fun onQuizFailed(questionId: String, nowMillis: Long = System.currentTimeMillis()): Long
}
