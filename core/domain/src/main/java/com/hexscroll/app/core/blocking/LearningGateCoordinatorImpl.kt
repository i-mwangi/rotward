package com.hexscroll.app.core.blocking

import com.hexscroll.app.core.model.LearningGateDecision
import com.hexscroll.app.core.repository.QuestionRepository
import com.hexscroll.app.core.repository.UserSettingsStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningGateCoordinatorImpl @Inject constructor(
    private val userSettingsStore: UserSettingsStore,
    private val questionRepository: QuestionRepository,
) : LearningGateCoordinator {

    override suspend fun evaluateBlock(nowMillis: Long): LearningGateDecision {
        if (!userSettingsStore.isLearningGateEnabled()) {
            return LearningGateDecision.PassThrough
        }

        val hauntCooldownUntil = userSettingsStore.getHauntCooldownUntil()
        if (nowMillis < hauntCooldownUntil) {
            return LearningGateDecision.ShowHauntLockout(hauntCooldownUntil - nowMillis)
        }

        val selectedTopics = userSettingsStore.getSelectedTopics()
        if (selectedTopics.isEmpty()) {
            return LearningGateDecision.PassThrough
        }

        questionRepository.ensureSeeded()
        val question = questionRepository.getNextQuestion(selectedTopics)
            ?: return LearningGateDecision.PassThrough

        return LearningGateDecision.ShowQuiz(question)
    }

    override suspend fun onQuizPassed(questionId: String, nowMillis: Long): Long {
        questionRepository.recordAnswer(questionId, isCorrect = true, nowMillis)
        val rewardMillis = userSettingsStore.getQuizRewardMillis()
        val pauseUntil = nowMillis + rewardMillis
        userSettingsStore.setPauseUntil(pauseUntil)
        return pauseUntil
    }

    override suspend fun onQuizFailed(questionId: String, nowMillis: Long): Long {
        questionRepository.recordAnswer(questionId, isCorrect = false, nowMillis)
        val cooldownMillis = userSettingsStore.getHauntCooldownMillis()
        val cooldownUntil = nowMillis + cooldownMillis
        userSettingsStore.setHauntCooldownUntil(cooldownUntil)
        return cooldownUntil
    }
}
