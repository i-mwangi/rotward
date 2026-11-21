package com.hexscroll.app.ui.home
import com.hexscroll.app.core.model.BlockOption
import com.hexscroll.app.core.model.LearningTopic
data class HomeUiState(
    val hasLoadedSettings: Boolean = false,
    val blockOption: BlockOption = BlockOption.NothingSelected,
    val timeLimit: Long = 900_000L,
    val timerOverlayEnabled: Boolean = true,
    val pauseUntil: Long = 0L,
    val learningGateEnabled: Boolean = false,
    val selectedTopics: Set<LearningTopic> = emptySet(),
    val hasCompletedTopicOnboarding: Boolean = false,
    val hasSeenAccessibilityExplainer: Boolean = false,
    val isAccessibilityServiceActive: Boolean = false,
    val totalDailyUsageMillis: Long = 0L,
    val reelsDailyUsageMillis: Long = 0L,
    val shortsDailyUsageMillis: Long = 0L,
    val tiktokDailyUsageMillis: Long = 0L,
    val facebookReelsDailyUsageMillis: Long = 0L,
    val hauntCooldownUntil: Long = 0L,
)