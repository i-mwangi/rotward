package com.hexscroll.app.core.data.repository

import com.hexscroll.app.core.data.database.dao.SessionSegmentDao
import com.hexscroll.app.core.data.database.model.SessionSegmentEntity
import com.hexscroll.app.core.model.BlockableApp
import com.hexscroll.app.core.repository.SessionTracker
import com.hexscroll.app.core.repository.UserSettingsStore
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionTrackerImpl @Inject constructor(
    private val userSettingsStore: UserSettingsStore,
    private val sessionSegmentDao: SessionSegmentDao,
) : SessionTracker {

    private var currentApp: BlockableApp? = null
    private var sessionStartMillis: Long = 0L

    override suspend fun onAppOpen(app: BlockableApp) {
        checkAndResetDailyUsageIfNeeded()
        currentApp = app
        sessionStartMillis = System.currentTimeMillis()
        Timber.d("Session opened for ${app.name}")
    }

    override suspend fun onAppClose() {
        currentApp = null
        sessionStartMillis = 0L
    }

    override suspend fun addToDailyUsage(app: BlockableApp, durationMillis: Long) {
        if (durationMillis <= 0L) return
        val existing = userSettingsStore.getAppDailyUsage(app)
        userSettingsStore.setAppDailyUsage(app, existing + durationMillis)
        val total = userSettingsStore.getTotalDailyUsage()
        userSettingsStore.setTotalDailyUsage(total + durationMillis)
        sessionSegmentDao.insert(
            SessionSegmentEntity(
                app = app.name,
                durationMillis = durationMillis,
                startDateTime = LocalDateTime.now().minusNanos(durationMillis * 1_000_000L),
            )
        )
    }

    override suspend fun getCurrentApp(): BlockableApp? = currentApp

    private suspend fun checkAndResetDailyUsageIfNeeded() {
        val lastReset = userSettingsStore.getLastResetDay()
        val today = LocalDate.now()
        if (lastReset != today) {
            userSettingsStore.resetAllDailyUsage()
            userSettingsStore.setLastResetDay(today)
            Timber.d("Daily usage reset for $today")
        }
    }
}
