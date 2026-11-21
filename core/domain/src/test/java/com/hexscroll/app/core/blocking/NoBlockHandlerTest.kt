package com.hexscroll.app.core.blocking

import com.hexscroll.app.core.blocking.handler.NoBlockHandler
import com.hexscroll.app.core.model.BlockingResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NoBlockHandlerTest {
    private val handler = NoBlockHandler()

    @Test
    fun `always returns Allow regardless of elapsed time`() = runTest {
        assertEquals(BlockingResult.Allow, handler.onPeriodicCheck(0L, 1000L))
        assertEquals(BlockingResult.Allow, handler.onPeriodicCheck(999_999L, 999_999L))
    }

    @Test
    fun `onExitBlockedContent does not throw`() = runTest {
        handler.onExitBlockedContent(60_000L, System.currentTimeMillis())
    }
}
