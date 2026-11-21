package com.hexscroll.app.core.blocking

import com.hexscroll.app.core.FakeUserSettingsStore
import com.hexscroll.app.core.blocking.handler.IntervalTimerBlockHandler
import com.hexscroll.app.core.model.BlockingResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IntervalTimerBlockHandlerTest {
    private lateinit var store: FakeUserSettingsStore
    private lateinit var handler: IntervalTimerBlockHandler

    @Before
    fun setUp() {
        store = FakeUserSettingsStore().apply {
            intervalAllowance = 300_000L  // 5 min
            intervalLength = 900_000L     // 15 min break
        }
        handler = IntervalTimerBlockHandler(store)
    }

    @Test
    fun `first check with no window starts a new window and allows`() = runTest {
        store.intervalWindowStart = 0L
        val result = handler.onPeriodicCheck(elapsedSessionMillis = 1_000L, nowMillis = 1_000L)
        assertTrue(result is BlockingResult.AllowWithRemaining)
        assertTrue(store.intervalWindowStart > 0L)
    }

    @Test
    fun `blocks when usage in window exceeds allowance and break not over`() = runTest {
        val now = 1_000_000L
        store.intervalWindowStart = now - 60_000L
        store.intervalUsage = 290_000L
        store.intervalAllowance = 300_000L
        store.intervalLength = 900_000L
        val result = handler.onPeriodicCheck(elapsedSessionMillis = 20_000L, nowMillis = now)
        assertTrue(result is BlockingResult.Block)
    }

    @Test
    fun `allows after break window elapses and resets window`() = runTest {
        val now = 2_000_000L
        store.intervalWindowStart = now - 1_300_000L // started 21.6 min ago
        store.intervalUsage = 300_000L               // hit allowance
        store.intervalAllowance = 300_000L
        store.intervalLength = 900_000L
        val result = handler.onPeriodicCheck(elapsedSessionMillis = 0L, nowMillis = now)
        assertTrue(result is BlockingResult.AllowWithRemaining)
        // Window should have been reset to now
        assertTrue(store.intervalWindowStart >= now - 1000L)
    }

    @Test
    fun `accumulates usage on exit`() = runTest {
        store.intervalUsage = 50_000L
        handler.onExitBlockedContent(sessionMillis = 30_000L, nowMillis = 0L)
        assertTrue(store.intervalUsage == 80_000L)
    }
}
