package com.hexscroll.app.core.blocking.handler

data class IntervalTimerState(
    val windowStartMillis: Long,
    val usageInWindowMillis: Long,
    val allowanceMillis: Long,
    val breakMillis: Long,
)
