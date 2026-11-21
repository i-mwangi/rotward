package com.hexscroll.app.core.blocking

import com.hexscroll.app.core.model.BlockingResult

interface BlockOptionHandler {
    suspend fun onPeriodicCheck(elapsedSessionMillis: Long, nowMillis: Long): BlockingResult
    suspend fun onExitBlockedContent(sessionMillis: Long, nowMillis: Long)
}
