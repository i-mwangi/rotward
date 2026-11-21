package com.hexscroll.app.util

import android.content.Context
import android.content.Intent
import android.provider.Settings

fun Context.isAccessibilityServiceEnabled(serviceClass: Class<*>): Boolean {
    val expected = "${packageName}/${serviceClass.name}"
    val enabled = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: return false
    return enabled.split(":").any { it.equals(expected, ignoreCase = true) }
}

fun Context.openAccessibilitySettings() {
    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
}
