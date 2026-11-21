package com.hexscroll.app.core.repository

import com.hexscroll.app.core.model.BlockOption
import com.hexscroll.app.core.model.LearningTopic
import kotlinx.coroutines.flow.Flow

interface UserSettingsStore {
    // Block option
    fun getBlockOptionFlow(): Flow<BlockOption>
    suspend fun getBlockOption(): BlockOption
    suspend fun setBlockOption(option: BlockOption)

    // Time limit
    fun getTimeLimitFlow(): Flow<Long>
    suspend fun getTimeLimit(): Long
    suspend fun setTimeLimit(millis: Long)

    // Interval timer
    suspend fun getIntervalLength(): Long
    suspend fun setIntervalLength(millis: Long)
    suspend fun getIntervalAllowance(): Long
    suspend fun setIntervalAllowance(millis: Long)
    suspend fun getIntervalWindowStartAt(): Long
    suspend fun setIntervalWindowStartAt(millis: Long)
    suspend fun getIntervalUsage(): Long
    suspend fun setIntervalUsage(millis: Long)

    // Timer overlay
    fun getTimerOverlayEnabledFlow(): Flow<Boolean>
    suspend fun isTimerOverlayEnabled(): Boolean
    suspend fun setTimerOverlayEnabled(enabled: Boolean)
    suspend fun getTimerOverlayX(): Int
    suspend fun setTimerOverlayX(x: Int)
    suspend fun getTimerOverlayY(): Int
    suspend fun setTimerOverlayY(y: Int)

    // Daily usage
    suspend fun getTotalDailyUsage(): Long
    suspend fun setTotalDailyUsage(millis: Long)
    suspend fun getAppDailyUsage(app: com.hexscroll.app.core.model.BlockableApp): Long
    suspend fun setAppDailyUsage(app: com.hexscroll.app.core.model.BlockableApp, millis: Long)
    suspend fun resetAllDailyUsage()
    suspend fun getLastResetDay(): java.time.LocalDate
    suspend fun setLastResetDay(day: java.time.LocalDate)

    // Accessibility explainer
    suspend fun hasSeenAccessibilityExplainer(): Boolean
    suspend fun setHasSeenAccessibilityExplainer(seen: Boolean)

    // Pause until (grace window)
    fun getPauseUntilFlow(): Flow<Long>
    suspend fun getPauseUntil(): Long
    suspend fun setPauseUntil(millis: Long)

    // Learning gate
    fun getLearningGateEnabledFlow(): Flow<Boolean>
    suspend fun isLearningGateEnabled(): Boolean
    suspend fun setLearningGateEnabled(enabled: Boolean)

    // Topics
    fun getSelectedTopicsFlow(): Flow<Set<LearningTopic>>
    suspend fun getSelectedTopics(): Set<LearningTopic>
    suspend fun setSelectedTopics(topics: Set<LearningTopic>)

    // Topic onboarding
    suspend fun hasCompletedTopicOnboarding(): Boolean
    suspend fun setHasCompletedTopicOnboarding(completed: Boolean)

    // Haunt cooldown
    suspend fun getHauntCooldownUntil(): Long
    suspend fun setHauntCooldownUntil(millis: Long)
    suspend fun getHauntCooldownMillis(): Long
    suspend fun setHauntCooldownMillis(millis: Long)

    // Quiz reward
    suspend fun getQuizRewardMillis(): Long
    suspend fun setQuizRewardMillis(millis: Long)
}
