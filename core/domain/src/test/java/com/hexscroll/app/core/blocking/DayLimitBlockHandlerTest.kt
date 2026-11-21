package com.hexscroll.app.core.blocking

import com.hexscroll.app.core.FakeUserSettingsStore
import com.hexscroll.app.core.blocking.handler.DayLimitBlockHandler
import com.hexscroll.app.core.model.BlockingResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DayLimitBlockHandlerTest {
    private lateinit var store: FakeUserSettingsStore
    private lateinit var handler: DayLimitBlockHandler

    @Before
    fun setUp() {
        store = FakeUserSettingsStore()
        handler = DayLimitBlockHandler(store)
    }

    @Test
    fun `allows when total usage plus elapsed is below limit`() = runTest {
        store.timeLimit = 60_000L
        store.totalDailyUsage = 10_000L
        val result = handler.onPeriodicCheck(elapsedSessionMillis = 20_000L, nowMillis = 0L)
        assertTrue(result is BlockingResult.AllowWithRemaining)
        assertEquals(30_000L, (result as BlockingResult.AllowWithRemaining).remainingMillis)
    }

    @Test
    fun `blocks when usage exactly meets limit`() = runTest {
        store.timeLimit = 60_000L
        store.totalDailyUsage = 50_000L
        val result = handler.onPeriodicCheck(elapsedSessionMillis = 10_000L, nowMillis = 0L)
        assertTrue(result is BlockingResult.Block)
    }

    @Test
    fun `blocks when usage exceeds limit`() = runTest {
        store.timeLimit = 60_000L
        store.totalDailyUsage = 60_000L
        val result = handler.onPeriodicCheck(elapsedSessionMillis = 5_000L, nowMillis = 0L)
        assertTrue(result is BlockingResult.Block)
    }

    @Test
    fun `remaining is correct when just under limit`() = runTest {
        store.timeLimit = 100_000L
        store.totalDailyUsage = 0L
        val result = handler.onPeriodicCheck(elapsedSessionMillis = 1L, nowMillis = 0L) as BlockingResult.AllowWithRemaining
        assertEquals(99_999L, result.remainingMillis)
    }
}
