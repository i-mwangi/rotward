package com.hexscroll.app.core.blocking

import com.hexscroll.app.core.blocking.handler.BlockAllBlockHandler
import com.hexscroll.app.core.model.BlockingResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class BlockAllBlockHandlerTest {
    private val handler = BlockAllBlockHandler()

    @Test
    fun `always blocks regardless of elapsed time`() = runTest {
        val result = handler.onPeriodicCheck(0L, 1000L)
        assertTrue(result is BlockingResult.Block)
    }

    @Test
    fun `blocks even with zero elapsed time`() = runTest {
        val result = handler.onPeriodicCheck(0L, 0L)
        assertTrue(result is BlockingResult.Block)
    }

    @Test
    fun `block reason is non-blank`() = runTest {
        val result = handler.onPeriodicCheck(100L, 100L) as BlockingResult.Block
        assertTrue(result.reason.isNotBlank())
    }
}
