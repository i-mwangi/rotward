package com.hexscroll.app.core.model

import java.time.LocalDateTime

data class SessionSegment(
    val id: Long = 0,
    val app: BlockableApp,
    val durationMillis: Long,
    val startDateTime: LocalDateTime,
)
