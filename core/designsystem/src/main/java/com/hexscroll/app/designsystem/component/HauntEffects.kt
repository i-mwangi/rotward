package com.hexscroll.app.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexscroll.app.designsystem.theme.BoneWhite
import com.hexscroll.app.designsystem.theme.CrimsonAccent
import com.hexscroll.app.designsystem.theme.CyanGlitch
import com.hexscroll.app.designsystem.theme.HauntBackground
import com.hexscroll.app.designsystem.theme.HexScrollTheme

/**
 * Draws subtle horizontal CRT scanlines and a radial dark vignette over its content.
 */
@Composable
fun CrtScanlineOverlay(
    modifier: Modifier = Modifier,
    scanlineColor: Color = Color(0x12FFFFFF),
    vignetteColor: Color = Color(0xBB000000),
    scanlineSpacing: Float = 4f,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawScanlines(scanlineColor, scanlineSpacing)
        drawVignette(vignetteColor)
    }
}

private fun DrawScope.drawScanlines(color: Color, spacing: Float) {
    var y = 0f
    while (y < size.height) {
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1f,
        )
        y += spacing
    }
}

private fun DrawScope.drawVignette(color: Color) {
    val radius = maxOf(size.width, size.height) * 0.75f
    drawCircle(
        color = color,
        radius = radius,
        center = Offset(size.width / 2f, size.height / 2f),
        alpha = 0.6f,
    )
}

/**
 * Renders text with three layered passes (cyan offset left, crimson offset right,
 * bone-white center) with animated micro-jitter for a chromatic aberration glitch effect.
 */
@Composable
fun GlitchOffsetText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
    ),
    cyanColor: Color = CyanGlitch,
    crimsonColor: Color = CrimsonAccent,
    centerColor: Color = BoneWhite,
    jitterAmplitudeDp: Float = 3f,
) {
    val transition = rememberInfiniteTransition(label = "glitch")

    val offsetX by transition.animateFloat(
        initialValue = -jitterAmplitudeDp,
        targetValue = jitterAmplitudeDp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 80, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glitchX",
    )
    val offsetY by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 120, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glitchY",
    )

    Box(modifier = modifier) {
        // Cyan shadow (left)
        Text(
            text = text,
            style = style.copy(color = cyanColor),
            modifier = Modifier.align(Alignment.Center)
                .padding(start = offsetX.coerceAtLeast(0f).dp),
        )
        // Crimson shadow (right)
        Text(
            text = text,
            style = style.copy(color = crimsonColor),
            modifier = Modifier.align(Alignment.Center)
                .padding(end = offsetX.coerceAtLeast(0f).dp, top = offsetY.coerceAtLeast(0f).dp),
        )
        // Center (bone white — the "real" text)
        Text(
            text = text,
            style = style.copy(color = centerColor),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

/**
 * A container that applies a randomized subtle alpha flicker pulse,
 * simulating an old CRT screen's instability.
 */
@Composable
fun ScreenFlickerBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = HauntBackground,
    content: @Composable BoxScope.() -> Unit,
) {
    val transition = rememberInfiniteTransition(label = "flicker")
    val alpha by transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "flickerAlpha",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .alpha(alpha),
        content = content,
    )
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun GlitchOffsetTextPreview() {
    HexScrollTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(HauntBackground)
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            GlitchOffsetText(
                text = "STILL CURSED",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                ),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CrtScanlineOverlayPreview() {
    HexScrollTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(HauntBackground),
        ) {
            CrtScanlineOverlay()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ScreenFlickerBoxPreview() {
    HexScrollTheme {
        ScreenFlickerBox {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                GlitchOffsetText(text = "YOU FAILED")
            }
        }
    }
}
