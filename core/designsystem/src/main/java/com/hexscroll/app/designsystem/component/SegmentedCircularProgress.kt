package com.hexscroll.app.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexscroll.app.designsystem.theme.CrimsonAccent
import com.hexscroll.app.designsystem.theme.EmeraldPass
import com.hexscroll.app.designsystem.theme.HexScrollTheme
import com.hexscroll.app.designsystem.theme.SurfaceCard
import com.hexscroll.app.designsystem.theme.TextSecondary

data class AppUsageArc(
    val label: String,
    val usageMillis: Long,
    val limitMillis: Long,
    val color: Color,
)

@Composable
fun SegmentedCircularProgress(
    arcs: List<AppUsageArc>,
    centerLabel: String,
    centerSublabel: String = "",
    modifier: Modifier = Modifier,
    size: Dp = 220.dp,
    strokeWidth: Dp = 14.dp,
    trackColor: Color = SurfaceCard,
    gapDegrees: Float = 6f,
) {
    val segmentCount = arcs.size.coerceAtLeast(1)
    val totalDegrees = 360f - (gapDegrees * segmentCount)
    val segmentDegrees = totalDegrees / segmentCount
    val animatedFractions = arcs.map { arc ->
        val fraction = if (arc.limitMillis > 0) {
            (arc.usageMillis.toFloat() / arc.limitMillis.toFloat()).coerceIn(0f, 1f)
        } else 0f
        val animated by animateFloatAsState(
            targetValue = fraction,
            animationSpec = tween(durationMillis = 800),
            label = "arc_${arc.label}",
        )
        animated
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(size)) {
                val strokePx = strokeWidth.toPx()
                val diameter = this.size.minDimension - strokePx
                val topLeft = Offset(strokePx / 2f, strokePx / 2f)
                val arcSize = Size(diameter, diameter)

                arcs.forEachIndexed { i, arc ->
                    val startAngle = -90f + i * (segmentDegrees + gapDegrees)

                    // Track
                    drawArc(
                        color = trackColor,
                        startAngle = startAngle,
                        sweepAngle = segmentDegrees,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(strokePx, cap = StrokeCap.Round),
                    )

                    // Progress
                    if (animatedFractions[i] > 0f) {
                        drawArc(
                            color = arc.color,
                            startAngle = startAngle,
                            sweepAngle = segmentDegrees * animatedFractions[i],
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(strokePx, cap = StrokeCap.Round),
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AutoResizingText(
                    text = centerLabel,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxFontSize = 22.sp,
                    modifier = Modifier.padding(horizontal = 32.dp),
                )
                if (centerSublabel.isNotBlank()) {
                    Text(
                        text = centerSublabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        AppUsageLegend(arcs = arcs)
    }
}

@Composable
fun AppUsageLegend(
    arcs: List<AppUsageArc>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        arcs.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                row.forEach { arc ->
                    LegendItem(arc = arc, modifier = Modifier.weight(1f))
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun LegendItem(arc: AppUsageArc, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(modifier = Modifier.size(10.dp)) {
            drawCircle(color = arc.color)
        }
        Spacer(Modifier.width(6.dp))
        Column {
            Text(
                text = arc.label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
            )
            Text(
                text = formatMillis(arc.usageMillis),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

private fun formatMillis(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) "%dh %02dm".format(hours, minutes)
    else if (minutes > 0) "%dm %02ds".format(minutes, seconds)
    else "%ds".format(seconds)
}

@Preview(showBackground = true, backgroundColor = 0xFF050507)
@Composable
private fun SegmentedCircularProgressPreview() {
    HexScrollTheme {
        SegmentedCircularProgress(
            arcs = listOf(
                AppUsageArc("Reels", 480_000L, 900_000L, CrimsonAccent),
                AppUsageArc("Shorts", 120_000L, 900_000L, Color(0xFF4FC3F7)),
                AppUsageArc("TikTok", 300_000L, 900_000L, Color(0xFFFF6B9D)),
                AppUsageArc("FB Reels", 60_000L, 900_000L, Color(0xFF64B5F6)),
            ),
            centerLabel = "16m 0s",
            centerSublabel = "today",
        )
    }
}
