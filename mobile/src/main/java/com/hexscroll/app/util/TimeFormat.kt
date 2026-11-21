package com.hexscroll.app.util

fun formatMillisToTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return if (h > 0) "%dh %02dm".format(h, m) else if (m > 0) "%dm %02ds".format(m, s) else "%ds".format(s)
}
