package com.hexscroll.app.core.blocking.handler

import com.hexscroll.app.core.blocking.BlockOptionHandler
import com.hexscroll.app.core.model.BlockingResult

class BlockAllBlockHandler : BlockOptionHandler {
    override suspend fun onPeriodicCheck(elapsedSessionMillis: Long, nowMillis: Long): BlockingResult =
        BlockingResult.Block("BlockAll active")

    override suspend fun onExitBlockedContent(sessionMillis: Long, nowMillis: Long) = Unit
}
