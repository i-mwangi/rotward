package com.hexscroll.app.core.model

enum class BlockableApp(
    val packageId: String,
    val viewIdHints: List<String>,
    val exitAction: ExitStrategy,
) {
    REELS(
        packageId = "com.instagram.android",
        viewIdHints = listOf(
            "com.instagram.android:id/clips_viewer_view_pager",
            "com.instagram.android:id/clips_video_container",
        ),
        exitAction = ExitStrategy.BACK,
    ),
    SHORTS(
        packageId = "com.google.android.youtube",
        viewIdHints = listOf(
            "com.google.android.youtube:id/reel_recycler",
            "com.google.android.youtube:id/reel_watch_fragment_root",
        ),
        exitAction = ExitStrategy.BACK,
    ),
    TIKTOK(
        packageId = "com.zhiliaoapp.musically",
        viewIdHints = emptyList(),
        exitAction = ExitStrategy.HOME,
    ),
    FACEBOOK_REELS(
        packageId = "com.facebook.katana",
        viewIdHints = listOf(
            "com.facebook.katana:id/reels_viewer",
        ),
        exitAction = ExitStrategy.BACK,
    );

    companion object {
        /** Also match TikTok regional variant */
        const val TIKTOK_ALT_PACKAGE = "com.ss.android.ugc.trill"

        fun fromPackage(packageName: String): BlockableApp? =
            entries.firstOrNull { it.packageId == packageName }
                ?: if (packageName == TIKTOK_ALT_PACKAGE) TIKTOK else null
    }
}

enum class ExitStrategy { BACK, HOME }
