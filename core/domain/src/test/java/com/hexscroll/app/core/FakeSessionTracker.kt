package com.hexscroll.app.core

import com.hexscroll.app.core.model.BlockableApp
import com.hexscroll.app.core.repository.SessionTracker

class FakeSessionTracker : SessionTracker {
    var currentApp: BlockableApp? = null
    val openEvents = mutableListOf<BlockableApp>()
    var closedCount = 0
    val usageAdded = mutableMapOf<BlockableApp, Long>()

    override suspend fun onAppOpen(app: BlockableApp) {
        currentApp = app
        openEvents += app
    }

    override suspend fun onAppClose() {
        currentApp = null
        closedCount++
    }

    override suspend fun addToDailyUsage(app: BlockableApp, durationMillis: Long) {
        usageAdded[app] = (usageAdded[app] ?: 0L) + durationMillis
    }

    override suspend fun getCurrentApp(): BlockableApp? = currentApp
}
