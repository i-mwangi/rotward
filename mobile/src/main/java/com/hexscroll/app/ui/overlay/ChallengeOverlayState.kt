package com.hexscroll.app.ui.overlay
import com.hexscroll.app.core.model.BlockableApp
import com.hexscroll.app.core.model.Question
sealed interface ChallengeOverlayState {
    data class Quiz(val question: Question, val targetApp: BlockableApp, val timeLimitSeconds: Int = 20) : ChallengeOverlayState
    data class Haunting(
        val headline: String,
        val subtext: String,
        val correctAnswer: String? = null,
        val explanation: String? = null,
        val lockoutSeconds: Int = 5,
        val cooldownRemainingMillis: Long? = null,
    ) : ChallengeOverlayState
}