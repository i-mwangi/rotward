package com.hexscroll.app.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.hexscroll.app.core.data.database.model.UserSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao : BaseDao<UserSettingsEntity> {

    @Query("SELECT * FROM user_settings WHERE id = 1 LIMIT 1")
    fun observeSettings(): Flow<UserSettingsEntity?>

    @Query("SELECT * FROM user_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): UserSettingsEntity?

    @Query("UPDATE user_settings SET active_block_option = :option WHERE id = 1")
    suspend fun setBlockOption(option: String)

    @Query("UPDATE user_settings SET time_limit = :millis WHERE id = 1")
    suspend fun setTimeLimit(millis: Long)

    @Query("UPDATE user_settings SET interval_length = :millis WHERE id = 1")
    suspend fun setIntervalLength(millis: Long)

    @Query("UPDATE user_settings SET interval_allowance = :millis WHERE id = 1")
    suspend fun setIntervalAllowance(millis: Long)

    @Query("UPDATE user_settings SET interval_window_start_at = :millis WHERE id = 1")
    suspend fun setIntervalWindowStartAt(millis: Long)

    @Query("UPDATE user_settings SET interval_usage = :millis WHERE id = 1")
    suspend fun setIntervalUsage(millis: Long)

    @Query("UPDATE user_settings SET timer_overlay_enabled = :enabled WHERE id = 1")
    suspend fun setTimerOverlayEnabled(enabled: Boolean)

    @Query("UPDATE user_settings SET timer_overlay_x = :x WHERE id = 1")
    suspend fun setTimerOverlayX(x: Int)

    @Query("UPDATE user_settings SET timer_overlay_y = :y WHERE id = 1")
    suspend fun setTimerOverlayY(y: Int)

    @Query("UPDATE user_settings SET total_daily_usage = :millis WHERE id = 1")
    suspend fun setTotalDailyUsage(millis: Long)

    @Query("UPDATE user_settings SET reels_daily_usage = :millis WHERE id = 1")
    suspend fun setReelsDailyUsage(millis: Long)

    @Query("UPDATE user_settings SET shorts_daily_usage = :millis WHERE id = 1")
    suspend fun setShortsDailyUsage(millis: Long)

    @Query("UPDATE user_settings SET tiktok_daily_usage = :millis WHERE id = 1")
    suspend fun setTiktokDailyUsage(millis: Long)

    @Query("UPDATE user_settings SET facebook_reels_daily_usage = :millis WHERE id = 1")
    suspend fun setFacebookReelsDailyUsage(millis: Long)

    @Query("UPDATE user_settings SET total_daily_usage = 0, reels_daily_usage = 0, shorts_daily_usage = 0, tiktok_daily_usage = 0, facebook_reels_daily_usage = 0 WHERE id = 1")
    suspend fun resetAllDailyUsage()

    @Query("UPDATE user_settings SET last_reset_day = :day WHERE id = 1")
    suspend fun setLastResetDay(day: String)

    @Query("UPDATE user_settings SET has_seen_accessibility_explainer = :seen WHERE id = 1")
    suspend fun setHasSeenAccessibilityExplainer(seen: Boolean)

    @Query("UPDATE user_settings SET pause_until_at = :millis WHERE id = 1")
    suspend fun setPauseUntil(millis: Long)

    @Query("UPDATE user_settings SET learning_gate_enabled = :enabled WHERE id = 1")
    suspend fun setLearningGateEnabled(enabled: Boolean)

    @Query("UPDATE user_settings SET selected_topics = :topics WHERE id = 1")
    suspend fun setSelectedTopics(topics: String)

    @Query("UPDATE user_settings SET has_completed_topic_onboarding = :completed WHERE id = 1")
    suspend fun setHasCompletedTopicOnboarding(completed: Boolean)

    @Query("UPDATE user_settings SET haunt_cooldown_until = :millis WHERE id = 1")
    suspend fun setHauntCooldownUntil(millis: Long)

    @Query("UPDATE user_settings SET haunt_cooldown_millis = :millis WHERE id = 1")
    suspend fun setHauntCooldownMillis(millis: Long)

    @Query("UPDATE user_settings SET quiz_reward_millis = :millis WHERE id = 1")
    suspend fun setQuizRewardMillis(millis: Long)
}
