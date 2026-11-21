package com.hexscroll.app.core.blocking.handler

import com.hexscroll.app.core.blocking.BlockOptionHandler
import com.hexscroll.app.core.model.BlockingResult

class NoBlockHandler : BlockOptionHandler {
    override suspend fun onPeriodicCheck(elapsedSessionMillis: Long, nowMillis: Long): BlockingResult =
        BlockingResult.Allow

    override suspend fun onExitBlockedContent(sessionMillis: Long, nowMillis: Long) = Unit
}
