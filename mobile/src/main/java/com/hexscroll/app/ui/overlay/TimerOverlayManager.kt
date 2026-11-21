package com.hexscroll.app.ui.overlay
import android.content.Context
import android.graphics.PixelFormat
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hexscroll.app.designsystem.theme.BoneWhite
import com.hexscroll.app.designsystem.theme.HexScrollTheme
import com.hexscroll.app.designsystem.theme.ObsidianCard
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class TimerOverlayManager @Inject constructor() {
    private var serviceContext: Context? = null
    private var windowManager: WindowManager? = null
    private var overlayView: FrameLayout? = null
    private var lifecycleOwner: OverlayLifecycleOwner? = null
    private val remainingMillis = mutableLongStateOf(0L)
    fun attachServiceContext(ctx: Context) {
        serviceContext = ctx
        windowManager = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    fun show(remaining: Long, x: Int = 0, y: Int = 120) {
        remainingMillis.longValue = remaining
        if (overlayView != null) return // already showing, just update value
        val ctx = serviceContext ?: return
        val lco = OverlayLifecycleOwner()
        lifecycleOwner = lco
        val composeView = ComposeView(ctx).apply {
            setContent {
                HexScrollTheme {
                    Box(
                        modifier = Modifier
                            .background(ObsidianCard.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                        val ms = remainingMillis.longValue
                        val mins = (ms / 60000).toInt()
                        val secs = ((ms % 60000) / 1000).toInt()
                        Text(
                            text = "%d:%02d".format(mins, secs),
                            style = MaterialTheme.typography.labelLarge,
                            color = BoneWhite,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
        val container = FrameLayout(ctx).apply {
            addView(composeView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
            ))
        }
        overlayView = container
        lco.attachToView(container)
        lco.onCreate()
        lco.onStart()
        lco.onResume()
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT,
        ).apply {
            this.x = x
            this.y = y
        }
        try {
            windowManager?.addView(container, params)
        } catch (e: Exception) {
            Timber.e(e, "Failed to add timer overlay")
            lco.onDestroy()
            overlayView = null
        }
    }
    fun update(remaining: Long) {
        remainingMillis.longValue = remaining
    }
    fun hide() {
        try {
            overlayView?.let { windowManager?.removeView(it) }
        } catch (e: Exception) {
            Timber.w(e, "Timer overlay remove failed")
        }
        lifecycleOwner?.onPause()
        lifecycleOwner?.onStop()
        lifecycleOwner?.onDestroy()
        overlayView = null
        lifecycleOwner = null
    }
    fun detach() {
        hide()
        serviceContext = null
        windowManager = null
    }
}