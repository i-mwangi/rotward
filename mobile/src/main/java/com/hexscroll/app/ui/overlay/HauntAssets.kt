package com.hexscroll.app.ui.overlay

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import timber.log.Timber

object HauntAssets {
    val images = listOf(
        "haunt/images/bats.png",
        "haunt/images/happy-monster.gif",
        "haunt/images/home-grass.png",
        "haunt/images/home-moon.png",
        "haunt/images/home-point.png",
        "haunt/images/home-pumpkin.png",
        "haunt/images/home-shadow.png",
        "haunt/images/home-stones.png",
        "haunt/images/home-trees.png",
        "haunt/images/pumpkin_pile.png",
        "haunt/images/skulls.png",
        "haunt/images/logo.gif",
    )
    val sounds = listOf(
        "haunt/sounds/creepy_music.mp3",
        "haunt/sounds/creepy_music_1.mp3",
        "haunt/sounds/heartbeat.mp3",
        "haunt/sounds/owl_hoot.mp3",
        "haunt/sounds/wolf-howl.mp3",
    )
    fun randomImage() = images.random()
    fun randomSound() = sounds.random()
}

@Suppress("DEPRECATION")
fun playHauntSound(context: Context, assetPath: String) {
    try {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // STREAM_ALARM bypasses silent/vibrate — always plays loud
        val maxVol = am.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        am.setStreamVolume(AudioManager.STREAM_ALARM, maxVol, 0)
        val afd = context.assets.openFd(assetPath)
        val player = MediaPlayer()
        player.setAudioStreamType(AudioManager.STREAM_ALARM)
        player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        afd.close()
        player.prepare()
        player.setOnCompletionListener { it.release() }
        player.start()
    } catch (e: Exception) {
        Timber.w(e, "Haunt sound failed")
    }
}
