package com.hexscroll.app.core

import com.hexscroll.app.core.model.BlockableApp
import com.hexscroll.app.core.model.BlockOption
import com.hexscroll.app.core.model.LearningTopic
import com.hexscroll.app.core.repository.UserSettingsStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

class FakeUserSettingsStore : UserSettingsStore {
    var blockOption = BlockOption.NothingSelected
    private val blockOptionFlow = MutableStateFlow(blockOption)
    var timeLimit = 900_000L
    private val timeLimitFlow = MutableStateFlow(timeLimit)
    var intervalLength = 900_000L
    var intervalAllowance = 300_000L
    var intervalWindowStart = 0L
    var intervalUsage = 0L
    var timerOverlayEnabled = true
    private val timerOverlayEnabledFlow = MutableStateFlow(timerOverlayEnabled)
    var timerOverlayX = 0
    var timerOverlayY = 120
    var totalDailyUsage = 0L
    private val appUsageMap = mutableMapOf<BlockableApp, Long>()
    var lastResetDay: LocalDate = LocalDate.now()
    var hasSeenExplainer = false
    var pauseUntil = 0L
    private val pauseUntilFlow = MutableStateFlow(0L)
    var learningGateEnabled = true
    private val learningGateFlow = MutableStateFlow(true)
    var selectedTopics: Set<LearningTopic> = emptySet()
    private val selectedTopicsFlow = MutableStateFlow<Set<LearningTopic>>(emptySet())
    var hasCompletedOnboarding = false
    var hauntCooldownUntil = 0L
    var hauntCooldownMillis = 180_000L
    var quizRewardMillis = 300_000L

    override fun getBlockOptionFlow(): Flow<BlockOption> = blockOptionFlow
    override suspend fun getBlockOption(): BlockOption = blockOption
    override suspend fun setBlockOption(option: BlockOption) {
        blockOption = option
        blockOptionFlow.value = option
    }

    override fun getTimeLimitFlow(): Flow<Long> = timeLimitFlow
    override suspend fun getTimeLimit(): Long = timeLimit
    override suspend fun setTimeLimit(millis: Long) {
        timeLimit = millis
        timeLimitFlow.value = millis
    }

    override suspend fun getIntervalLength(): Long = intervalLength
    override suspend fun setIntervalLength(millis: Long) { intervalLength = millis }
    override suspend fun getIntervalAllowance(): Long = intervalAllowance
    override suspend fun setIntervalAllowance(millis: Long) { intervalAllowance = millis }
    override suspend fun getIntervalWindowStartAt(): Long = intervalWindowStart
    override suspend fun setIntervalWindowStartAt(millis: Long) { intervalWindowStart = millis }
    override suspend fun getIntervalUsage(): Long = intervalUsage
    override suspend fun setIntervalUsage(millis: Long) { intervalUsage = millis }

    override fun getTimerOverlayEnabledFlow(): Flow<Boolean> = timerOverlayEnabledFlow
    override suspend fun isTimerOverlayEnabled(): Boolean = timerOverlayEnabled
    override suspend fun setTimerOverlayEnabled(enabled: Boolean) {
        timerOverlayEnabled = enabled
        timerOverlayEnabledFlow.value = enabled
    }
    override suspend fun getTimerOverlayX(): Int = timerOverlayX
    override suspend fun setTimerOverlayX(x: Int) { timerOverlayX = x }
    override suspend fun getTimerOverlayY(): Int = timerOverlayY
    override suspend fun setTimerOverlayY(y: Int) { timerOverlayY = y }

    override suspend fun getTotalDailyUsage(): Long = totalDailyUsage
    override suspend fun setTotalDailyUsage(millis: Long) { totalDailyUsage = millis }
    override suspend fun getAppDailyUsage(app: BlockableApp): Long = appUsageMap[app] ?: 0L
    override suspend fun setAppDailyUsage(app: BlockableApp, millis: Long) { appUsageMap[app] = millis }
    override suspend fun resetAllDailyUsage() {
        totalDailyUsage = 0L
        appUsageMap.clear()
    }
    override suspend fun getLastResetDay(): LocalDate = lastResetDay
    override suspend fun setLastResetDay(day: LocalDate) { lastResetDay = day }

    override suspend fun hasSeenAccessibilityExplainer(): Boolean = hasSeenExplainer
    override suspend fun setHasSeenAccessibilityExplainer(seen: Boolean) { hasSeenExplainer = seen }

    override fun getPauseUntilFlow(): Flow<Long> = pauseUntilFlow
    override suspend fun getPauseUntil(): Long = pauseUntil
    override suspend fun setPauseUntil(millis: Long) {
        pauseUntil = millis
        pauseUntilFlow.value = millis
    }

    override fun getLearningGateEnabledFlow(): Flow<Boolean> = learningGateFlow
    override suspend fun isLearningGateEnabled(): Boolean = learningGateEnabled
    override suspend fun setLearningGateEnabled(enabled: Boolean) {
        learningGateEnabled = enabled
        learningGateFlow.value = enabled
    }

    override fun getSelectedTopicsFlow(): Flow<Set<LearningTopic>> = selectedTopicsFlow
    override suspend fun getSelectedTopics(): Set<LearningTopic> = selectedTopics
    override suspend fun setSelectedTopics(topics: Set<LearningTopic>) {
        selectedTopics = topics
        selectedTopicsFlow.value = topics
    }

    override suspend fun hasCompletedTopicOnboarding(): Boolean = hasCompletedOnboarding
    override suspend fun setHasCompletedTopicOnboarding(completed: Boolean) { hasCompletedOnboarding = completed }

    override suspend fun getHauntCooldownUntil(): Long = hauntCooldownUntil
    override suspend fun setHauntCooldownUntil(millis: Long) { hauntCooldownUntil = millis }
    override suspend fun getHauntCooldownMillis(): Long = hauntCooldownMillis
    override suspend fun setHauntCooldownMillis(millis: Long) { hauntCooldownMillis = millis }
    override suspend fun getQuizRewardMillis(): Long = quizRewardMillis
    override suspend fun setQuizRewardMillis(millis: Long) { quizRewardMillis = millis }
}
