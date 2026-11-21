package com.hexscroll.app.core.model

sealed interface LearningGateDecision {
    /** Learning gate is disabled or no valid topics/questions exist — fall back to instant exit. */
    data object PassThrough : LearningGateDecision

    /** User must answer this topic-scoped question to earn a grace window. */
    data class ShowQuiz(val question: Question) : LearningGateDecision

    /** User failed recently and is still inside the haunt cooldown window. */
    data class ShowHauntLockout(val remainingMillis: Long) : LearningGateDecision
}
