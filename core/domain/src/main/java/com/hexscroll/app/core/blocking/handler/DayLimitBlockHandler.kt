package com.hexscroll.app.core.blocking.handler

import com.hexscroll.app.core.blocking.BlockOptionHandler
import com.hexscroll.app.core.model.BlockingResult
import com.hexscroll.app.core.repository.UserSettingsStore

class DayLimitBlockHandler(
    private val userSettingsStore: UserSettingsStore,
) : BlockOptionHandler {
    override suspend fun onPeriodicCheck(elapsedSessionMillis: Long, nowMillis: Long): BlockingResult {
        val timeLimit = userSettingsStore.getTimeLimit()
        val totalDailyUsage = userSettingsStore.getTotalDailyUsage()
        val effectiveUsage = totalDailyUsage + elapsedSessionMillis
        return if (effectiveUsage >= timeLimit) {
            BlockingResult.Block("Daily limit of ${timeLimit}ms reached")
        } else {
            BlockingResult.AllowWithRemaining(timeLimit - effectiveUsage)
        }
    }

    override suspend fun onExitBlockedContent(sessionMillis: Long, nowMillis: Long) = Unit
}
