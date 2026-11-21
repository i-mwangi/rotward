package com.hexscroll.app.core.data.repository

import com.hexscroll.app.core.data.database.dao.UserSettingsDao
import com.hexscroll.app.core.data.database.model.UserSettingsEntity
import com.hexscroll.app.core.model.BlockableApp
import com.hexscroll.app.core.model.BlockOption
import com.hexscroll.app.core.model.LearningTopic
import com.hexscroll.app.core.repository.UserSettingsStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsStoreImpl @Inject constructor(
    private val dao: UserSettingsDao,
) : UserSettingsStore {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _settings = MutableStateFlow<UserSettingsEntity?>(null)

    init {
        runBlocking(Dispatchers.IO) {
            dao.insertIgnore(UserSettingsEntity(id = 1))
            _settings.value = dao.getSettings() ?: UserSettingsEntity(id = 1)
        }
        scope.launch {
            dao.observeSettings().filterNotNull().collect { entity ->
                _settings.value = entity
            }
        }
    }

    private fun current(): UserSettingsEntity = _settings.value ?: UserSettingsEntity(id = 1)

    override fun getBlockOptionFlow(): Flow<BlockOption> =
        _settings.filterNotNull().map { BlockOption.valueOf(it.activeBlockOption) }

    override suspend fun getBlockOption(): BlockOption =
        BlockOption.valueOf(current().activeBlockOption)

    override suspend fun setBlockOption(option: BlockOption) { dao.setBlockOption(option.name) }

    override fun getTimeLimitFlow(): Flow<Long> = _settings.filterNotNull().map { it.timeLimit }
    override suspend fun getTimeLimit(): Long = current().timeLimit
    override suspend fun setTimeLimit(millis: Long) { dao.setTimeLimit(millis) }

    override suspend fun getIntervalLength(): Long = current().intervalLength
    override suspend fun setIntervalLength(millis: Long) { dao.setIntervalLength(millis) }
    override suspend fun getIntervalAllowance(): Long = current().intervalAllowance
    override suspend fun setIntervalAllowance(millis: Long) { dao.setIntervalAllowance(millis) }
    override suspend fun getIntervalWindowStartAt(): Long = current().intervalWindowStartAt
    override suspend fun setIntervalWindowStartAt(millis: Long) { dao.setIntervalWindowStartAt(millis) }
    override suspend fun getIntervalUsage(): Long = current().intervalUsage
    override suspend fun setIntervalUsage(millis: Long) { dao.setIntervalUsage(millis) }

    override fun getTimerOverlayEnabledFlow(): Flow<Boolean> =
        _settings.filterNotNull().map { it.timerOverlayEnabled }
    override suspend fun isTimerOverlayEnabled(): Boolean = current().timerOverlayEnabled
    override suspend fun setTimerOverlayEnabled(enabled: Boolean) { dao.setTimerOverlayEnabled(enabled) }
    override suspend fun getTimerOverlayX(): Int = current().timerOverlayX
    override suspend fun setTimerOverlayX(x: Int) { dao.setTimerOverlayX(x) }
    override suspend fun getTimerOverlayY(): Int = current().timerOverlayY
    override suspend fun setTimerOverlayY(y: Int) { dao.setTimerOverlayY(y) }

    override suspend fun getTotalDailyUsage(): Long = current().totalDailyUsage
    override suspend fun setTotalDailyUsage(millis: Long) { dao.setTotalDailyUsage(millis) }

    override suspend fun getAppDailyUsage(app: BlockableApp): Long = when (app) {
        BlockableApp.REELS -> current().reelsDailyUsage
        BlockableApp.SHORTS -> current().shortsDailyUsage
        BlockableApp.TIKTOK -> current().tiktokDailyUsage
        BlockableApp.FACEBOOK_REELS -> current().facebookReelsDailyUsage
    }

    override suspend fun setAppDailyUsage(app: BlockableApp, millis: Long) = when (app) {
        BlockableApp.REELS -> dao.setReelsDailyUsage(millis)
        BlockableApp.SHORTS -> dao.setShortsDailyUsage(millis)
        BlockableApp.TIKTOK -> dao.setTiktokDailyUsage(millis)
        BlockableApp.FACEBOOK_REELS -> dao.setFacebookReelsDailyUsage(millis)
    }

    override suspend fun resetAllDailyUsage() { dao.resetAllDailyUsage() }
    override suspend fun getLastResetDay(): LocalDate = current().lastResetDay
    override suspend fun setLastResetDay(day: LocalDate) { dao.setLastResetDay(day.toString()) }

    override suspend fun hasSeenAccessibilityExplainer(): Boolean = current().hasSeenAccessibilityExplainer
    override suspend fun setHasSeenAccessibilityExplainer(seen: Boolean) { dao.setHasSeenAccessibilityExplainer(seen) }

    override fun getPauseUntilFlow(): Flow<Long> = _settings.filterNotNull().map { it.pauseUntilAt }
    override suspend fun getPauseUntil(): Long = current().pauseUntilAt
    override suspend fun setPauseUntil(millis: Long) { dao.setPauseUntil(millis) }

    override fun getLearningGateEnabledFlow(): Flow<Boolean> =
        _settings.filterNotNull().map { it.learningGateEnabled }
    override suspend fun isLearningGateEnabled(): Boolean = current().learningGateEnabled
    override suspend fun setLearningGateEnabled(enabled: Boolean) { dao.setLearningGateEnabled(enabled) }

    override fun getSelectedTopicsFlow(): Flow<Set<LearningTopic>> =
        _settings.filterNotNull().map { parseTopics(it.selectedTopics) }
    override suspend fun getSelectedTopics(): Set<LearningTopic> = parseTopics(current().selectedTopics)
    override suspend fun setSelectedTopics(topics: Set<LearningTopic>) {
        dao.setSelectedTopics(topics.joinToString(",") { it.id })
    }

    private fun parseTopics(raw: String): Set<LearningTopic> {
        if (raw.isBlank()) return emptySet()
        return raw.split(",").mapNotNull { LearningTopic.fromId(it.trim()) }.toSet()
    }

    override suspend fun hasCompletedTopicOnboarding(): Boolean = current().hasCompletedTopicOnboarding
    override suspend fun setHasCompletedTopicOnboarding(completed: Boolean) { dao.setHasCompletedTopicOnboarding(completed) }

    override suspend fun getHauntCooldownUntil(): Long = current().hauntCooldownUntil
    override suspend fun setHauntCooldownUntil(millis: Long) { dao.setHauntCooldownUntil(millis) }
    override suspend fun getHauntCooldownMillis(): Long = current().hauntCooldownMillis
    override suspend fun setHauntCooldownMillis(millis: Long) { dao.setHauntCooldownMillis(millis) }
    override suspend fun getQuizRewardMillis(): Long = current().quizRewardMillis
    override suspend fun setQuizRewardMillis(millis: Long) { dao.setQuizRewardMillis(millis) }
}
