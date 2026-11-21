package com.hexscroll.app.core.logging

import android.util.Log
import timber.log.Timber

class ReleaseLogTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority < Log.WARN) return
        // In production, forward WARN/ERROR to your crash reporter here.
        // For now, suppress below WARN entirely.
    }
}
