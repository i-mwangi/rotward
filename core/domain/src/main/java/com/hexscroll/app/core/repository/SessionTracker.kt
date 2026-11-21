package com.hexscroll.app.core.repository

import com.hexscroll.app.core.model.BlockableApp

interface SessionTracker {
    suspend fun onAppOpen(app: BlockableApp)
    suspend fun onAppClose()
    suspend fun addToDailyUsage(app: BlockableApp, durationMillis: Long)
    suspend fun getCurrentApp(): BlockableApp?
}
