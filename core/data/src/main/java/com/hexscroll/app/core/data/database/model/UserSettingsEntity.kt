package com.hexscroll.app.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey
    val id: Int = 1,

    @ColumnInfo(name = "active_block_option")
    val activeBlockOption: String = "NothingSelected",

    @ColumnInfo(name = "time_limit")
    val timeLimit: Long = 900_000L,

    @ColumnInfo(name = "interval_length")
    val intervalLength: Long = 900_000L,

    @ColumnInfo(name = "interval_allowance")
    val intervalAllowance: Long = 300_000L,

    @ColumnInfo(name = "interval_window_start_at")
    val intervalWindowStartAt: Long = 0L,

    @ColumnInfo(name = "interval_usage")
    val intervalUsage: Long = 0L,

    @ColumnInfo(name = "timer_overlay_enabled")
    val timerOverlayEnabled: Boolean = true,

    @ColumnInfo(name = "timer_overlay_x")
    val timerOverlayX: Int = 0,

    @ColumnInfo(name = "timer_overlay_y")
    val timerOverlayY: Int = 120,

    @ColumnInfo(name = "last_reset_day")
    val lastResetDay: LocalDate = LocalDate.now(),

    @ColumnInfo(name = "total_daily_usage")
    val totalDailyUsage: Long = 0L,

    @ColumnInfo(name = "reels_daily_usage")
    val reelsDailyUsage: Long = 0L,

    @ColumnInfo(name = "shorts_daily_usage")
    val shortsDailyUsage: Long = 0L,

    @ColumnInfo(name = "tiktok_daily_usage")
    val tiktokDailyUsage: Long = 0L,

    @ColumnInfo(name = "facebook_reels_daily_usage")
    val facebookReelsDailyUsage: Long = 0L,

    @ColumnInfo(name = "has_seen_accessibility_explainer")
    val hasSeenAccessibilityExplainer: Boolean = false,

    @ColumnInfo(name = "pause_until_at")
    val pauseUntilAt: Long = 0L,

    @ColumnInfo(name = "learning_gate_enabled")
    val learningGateEnabled: Boolean = true,

    @ColumnInfo(name = "selected_topics")
    val selectedTopics: String = "",

    @ColumnInfo(name = "has_completed_topic_onboarding")
    val hasCompletedTopicOnboarding: Boolean = false,

    @ColumnInfo(name = "haunt_cooldown_until")
    val hauntCooldownUntil: Long = 0L,

    @ColumnInfo(name = "quiz_reward_millis")
    val quizRewardMillis: Long = 300_000L,

    @ColumnInfo(name = "haunt_cooldown_millis")
    val hauntCooldownMillis: Long = 180_000L,
)
