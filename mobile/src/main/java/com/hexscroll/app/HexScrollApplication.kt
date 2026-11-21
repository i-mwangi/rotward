package com.hexscroll.app
import android.app.Application
import com.hexscroll.app.core.logging.DebugLogTree
import com.hexscroll.app.core.logging.ReleaseLogTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
@HiltAndroidApp
class HexScrollApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugLogTree()) else Timber.plant(ReleaseLogTree())
    }
}