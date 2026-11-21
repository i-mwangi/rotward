package com.hexscroll.app.core.model

sealed interface BlockingResult {
    data object Allow : BlockingResult
    data class Block(val reason: String) : BlockingResult
    data class AllowWithRemaining(val remainingMillis: Long) : BlockingResult
}
