package com.hexscroll.app.core.blocking.handler

import com.hexscroll.app.core.blocking.BlockOptionHandler
import com.hexscroll.app.core.model.BlockingResult
import com.hexscroll.app.core.repository.UserSettingsStore

class IntervalTimerBlockHandler(
    private val userSettingsStore: UserSettingsStore,
) : BlockOptionHandler {

    override suspend fun onPeriodicCheck(elapsedSessionMillis: Long, nowMillis: Long): BlockingResult {
        val allowance = userSettingsStore.getIntervalAllowance()
        val breakLength = userSettingsStore.getIntervalLength()
        val windowStart = userSettingsStore.getIntervalWindowStartAt()
        val usageInWindow = userSettingsStore.getIntervalUsage()

        // If no window has started yet, start one now
        if (windowStart == 0L) {
            userSettingsStore.setIntervalWindowStartAt(nowMillis)
            userSettingsStore.setIntervalUsage(elapsedSessionMillis)
            return BlockingResult.AllowWithRemaining(allowance - elapsedSessionMillis)
        }

        val effectiveUsage = usageInWindow + elapsedSessionMillis
        val windowEnd = windowStart + allowance + breakLength

        return when {
            effectiveUsage >= allowance -> {
                // Check if the break period is over
                val breakEndTime = windowStart + allowance + breakLength
                if (nowMillis >= breakEndTime) {
                    // Start a fresh window
                    userSettingsStore.setIntervalWindowStartAt(nowMillis)
                    userSettingsStore.setIntervalUsage(0L)
                    BlockingResult.AllowWithRemaining(allowance)
                } else {
                    BlockingResult.Block("Interval break active until $breakEndTime")
                }
            }
            else -> {
                userSettingsStore.setIntervalUsage(effectiveUsage)
                BlockingResult.AllowWithRemaining(allowance - effectiveUsage)
            }
        }
    }

    override suspend fun onExitBlockedContent(sessionMillis: Long, nowMillis: Long) {
        val currentUsage = userSettingsStore.getIntervalUsage()
        userSettingsStore.setIntervalUsage(currentUsage + sessionMillis)
    }
}
