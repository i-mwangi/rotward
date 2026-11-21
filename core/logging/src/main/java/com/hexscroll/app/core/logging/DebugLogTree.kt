package com.hexscroll.app.core.logging

import timber.log.Timber

class DebugLogTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String =
        "(${element.fileName}:${element.lineNumber})#${element.methodName}"
}
