package com.hexscroll.app.ui.overlay
import android.content.Context
import android.graphics.PixelFormat
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import com.hexscroll.app.core.model.BlockableApp
import com.hexscroll.app.core.model.Question
import com.hexscroll.app.designsystem.theme.HexScrollTheme
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class ChallengeOverlayManager @Inject constructor() {
    private var serviceContext: Context? = null
    private var windowManager: WindowManager? = null
    private var containerView: BackLockFrameLayout? = null
    private var lifecycleOwner: OverlayLifecycleOwner? = null
    private val overlayState = mutableStateOf<ChallengeOverlayState?>(null)
    fun attachServiceContext(ctx: Context) {
        serviceContext = ctx
        windowManager = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    fun showQuiz(
        question: Question,
        targetApp: BlockableApp,
        onPassed: () -> Unit,
        onHauntFinishedAfterFail: () -> Unit,
    ) {
        val ctx = serviceContext ?: return
        overlayState.value = ChallengeOverlayState.Quiz(question = question, targetApp = targetApp)
        val onAnswer: (Int) -> Unit = { selectedIndex ->
            val passed = selectedIndex == question.correctIndex
            if (passed) {
                overlayState.value = null
                onPassed()
            } else {
                val correctAnswer = question.options.getOrNull(question.correctIndex)
                overlayState.value = ChallengeOverlayState.Haunting(
                    headline = "WRONG ANSWER",
                    subtext = "Exiting in",
                    correctAnswer = correctAnswer,
                    explanation = question.explanation,
                    lockoutSeconds = 5,
                )
                triggerHapticAndAudio(ctx)
            }
        }
        showOverlayWindow(ctx) {
            ChallengeOverlayContent(
                state = overlayState.value ?: ChallengeOverlayState.Quiz(question, targetApp),
                onAnswerSelected = onAnswer,
                onHauntComplete = {
                    overlayState.value = null
                    onHauntFinishedAfterFail()
                },
            )
        }
    }
    fun showCooldownHaunt(remainingMillis: Long, onFinished: () -> Unit) {
        val ctx = serviceContext ?: return
        overlayState.value = ChallengeOverlayState.Haunting(
            headline = "STILL CURSED",
            subtext = "Exiting in",
            cooldownRemainingMillis = remainingMillis,
            lockoutSeconds = 3,
        )
        triggerHapticAndAudio(ctx)
        showOverlayWindow(ctx) {
            ChallengeOverlayContent(
                state = overlayState.value!!,
                onAnswerSelected = {},
                onHauntComplete = {
                    overlayState.value = null
                    onFinished()
                },
            )
        }
    }
    fun dismiss() {
        try {
            containerView?.let { windowManager?.removeView(it) }
        } catch (e: Exception) {
            Timber.e(e, "Error removing overlay view")
        }
        lifecycleOwner?.onPause()
        lifecycleOwner?.onStop()
        lifecycleOwner?.onDestroy()
        containerView = null
        lifecycleOwner = null
    }
    private fun showOverlayWindow(ctx: Context, content: @androidx.compose.runtime.Composable () -> Unit) {
        dismiss() // remove any existing overlay first
        val lco = OverlayLifecycleOwner()
        lifecycleOwner = lco
        val composeView = ComposeView(ctx).apply {
            setContent {
                HexScrollTheme { content() }
            }
        }
        val container = BackLockFrameLayout(ctx).apply {
            addView(composeView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
            ))
        }
        containerView = container
        lco.attachToView(container)
        lco.onCreate()
        lco.onStart()
        lco.onResume()
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT,
        )
        try {
            windowManager?.addView(container, params)
        } catch (e: Exception) {
            Timber.e(e, "Failed to add overlay view")
            lco.onDestroy()
        }
    }
    private fun triggerHapticAndAudio(ctx: Context) {
        // Haptics
        try {
            val waveform = longArrayOf(0, 90, 50, 140, 60, 220)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vm.defaultVibrator.vibrate(VibrationEffect.createWaveform(waveform, -1))
            } else {
                @Suppress("DEPRECATION")
                val v = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createWaveform(waveform, -1))
                } else {
                    @Suppress("DEPRECATION")
                    v.vibrate(waveform, -1)
                }
            }
        } catch (e: Exception) {
            Timber.w(e, "Haptic failed")
        }
        // Audio — only when ringer is normal (never blast on silent)
        try {
            val am = ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (am.ringerMode == AudioManager.RINGER_MODE_NORMAL) {
                val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 60)
                tg.startTone(ToneGenerator.TONE_PROP_NACK, 300)
            }
        } catch (e: Exception) {
            Timber.w(e, "Audio sting failed")
        }
    }
}
/** FrameLayout that swallows BACK and ESCAPE to prevent users dismissing the overlay. */
class BackLockFrameLayout(ctx: Context) : FrameLayout(ctx) {
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK || event.keyCode == KeyEvent.KEYCODE_ESCAPE) {
            return true // consume — do not propagate
        }
        return super.dispatchKeyEvent(event)
    }
}