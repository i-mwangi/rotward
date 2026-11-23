@file:OptIn(ExperimentalMaterial3Api::class)
package com.hexscroll.app.ui.home
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hexscroll.app.BuildConfig
import com.hexscroll.app.accessibility.HexScrollAccessibilityService
import com.hexscroll.app.core.model.BlockOption
import com.hexscroll.app.core.model.LearningTopic
import com.hexscroll.app.designsystem.theme.*
import com.hexscroll.app.ui.home.components.*
import com.hexscroll.app.util.isAccessibilityServiceEnabled
import com.hexscroll.app.util.openAccessibilitySettings

@Composable
fun HomeScreen(onNavigateToTopicPicker: (Boolean) -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showHelp by remember { mutableStateOf(false) }
    var showTimeLimitDialog by remember { mutableStateOf(false) }
    var showIntervalDialog by remember { mutableStateOf(false) }
    var showAccessibilityExplainer by remember { mutableStateOf(false) }
    var showAccessibilitySuccess by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.hasLoadedSettings) {
        if (uiState.hasLoadedSettings && !uiState.hasCompletedTopicOnboarding) onNavigateToTopicPicker(true)
        if (uiState.hasLoadedSettings) {
            viewModel.refreshAccessibilityStatus()
            if (!context.isAccessibilityServiceEnabled(HexScrollAccessibilityService::class.java) && !uiState.hasSeenAccessibilityExplainer)
                showAccessibilityExplainer = true
        }
    }
    if (showHelp) HelpDialog(onDismiss = { showHelp = false })
    if (showAccessibilityExplainer) AccessibilityExplainerDialog(
        onEnable = { showAccessibilityExplainer = false; viewModel.markAccessibilityExplainerSeen(); context.openAccessibilitySettings() },
        onDismiss = { showAccessibilityExplainer = false; viewModel.markAccessibilityExplainerSeen() })
    if (showAccessibilitySuccess) AccessibilitySuccessBottomSheet(onDismiss = { showAccessibilitySuccess = false })
    if (showTimeLimitDialog) TimeLimitDialog(currentMillis = uiState.timeLimit,
        onConfirm = { viewModel.setTimeLimit(it); showTimeLimitDialog = false }, onDismiss = { showTimeLimitDialog = false })
    if (showIntervalDialog) IntervalTimerDialog(currentAllowanceMillis = uiState.intervalAllowance, currentBreakMillis = uiState.intervalLength,
        onConfirm = { a, b -> viewModel.setIntervalSettings(a, b); showIntervalDialog = false }, onDismiss = { showIntervalDialog = false })
    Scaffold(
        topBar = { TopAppBar(title = { Text("HexScroll", fontWeight = FontWeight.Bold) },
            actions = { IconButton(onClick = { showHelp = true }) { Icon(Icons.Default.Help, contentDescription = "Help") } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)) },
        containerColor = Background,
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding).background(Brush.radialGradient(colors = listOf(HolographicElectricSky.copy(alpha = 0.08f), HolographicVioletGlow.copy(alpha = 0.06f), HolographicNeonMint.copy(alpha = 0.04f), MaterialTheme.colorScheme.background), radius = 1200f)))
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            // ── Donut hero ──────────────────────────────────────────────────
            UsageDonut(uiState = uiState)

            // ── Status pill ─────────────────────────────────────────────────
            val nowMs = System.currentTimeMillis()
            when {
                uiState.pauseUntil > nowMs -> StatusPill("✓ Grace window · ${fmtMs(uiState.pauseUntil - nowMs)} left", EmeraldPass)
                uiState.hauntCooldownUntil > nowMs -> StatusPill("💀 Cooldown · ${fmtMs(uiState.hauntCooldownUntil - nowMs)} remaining", CrimsonAccent)
            }

            // ── Accessibility warning ────────────────────────────────────────
            if (!uiState.isAccessibilityServiceActive) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CrimsonAccent.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(12.dp)) {
                    Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, null, tint = CrimsonAccent, modifier = Modifier.size(20.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Accessibility needed", style = MaterialTheme.typography.labelLarge, color = CrimsonAccent, fontWeight = FontWeight.Bold)
                            Text("Required to block feeds", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                        TextButton(onClick = { context.openAccessibilitySettings() }) { Text("Enable", color = CrimsonAccent, fontWeight = FontWeight.Bold) }
                    }
                }
            }

            // ── Blocking mode cards ──────────────────────────────────────────
            Text("Blocking Mode", style = MaterialTheme.typography.labelMedium, color = TextSecondary, modifier = Modifier.fillMaxWidth())
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ModeCard("Block All", Icons.Default.Block, BlockOption.BlockAll, uiState.blockOption, Modifier.weight(1f)) { viewModel.setBlockOption(it) }
                ModeCard("Daily Limit", Icons.Default.Timer, BlockOption.DailyLimit, uiState.blockOption, Modifier.weight(1f)) { viewModel.setBlockOption(it) }
                ModeCard("Interval", Icons.Default.Timelapse, BlockOption.IntervalTimer, uiState.blockOption, Modifier.weight(1f)) { viewModel.setBlockOption(it) }
                ModeCard("Off", Icons.Default.Block, BlockOption.NothingSelected, uiState.blockOption, Modifier.weight(1f)) { viewModel.setBlockOption(it) }
            }
            // Sub-label for active mode
            when (uiState.blockOption) {
                BlockOption.DailyLimit -> TextButton(onClick = { showTimeLimitDialog = true }, contentPadding = PaddingValues(0.dp)) {
                    Text("${uiState.timeLimit / 60000} min/day  ·  tap to change", color = CrimsonAccent, style = MaterialTheme.typography.labelMedium) }
                BlockOption.IntervalTimer -> TextButton(onClick = { showIntervalDialog = true }, contentPadding = PaddingValues(0.dp)) {
                    Text("${uiState.intervalAllowance / 60000}m on / ${uiState.intervalLength / 60000}m off  ·  tap to change", color = CrimsonAccent, style = MaterialTheme.typography.labelMedium) }
                else -> {}
            }

            // ── Learning Gate card ───────────────────────────────────────────
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(14.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column { Text("Learning Gate", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text("Answer a question to unlock scrolling", style = MaterialTheme.typography.bodySmall, color = TextSecondary) }
                        Switch(checked = uiState.learningGateEnabled, onCheckedChange = { viewModel.setLearningGateEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPass, checkedTrackColor = EmeraldPass.copy(alpha = 0.4f)))
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(if (uiState.selectedTopics.isEmpty()) "No topics selected" else "${uiState.selectedTopics.size} topics active",
                            style = MaterialTheme.typography.labelMedium, color = if (uiState.selectedTopics.isEmpty()) CrimsonAccent else EmeraldPass)
                        TextButton(onClick = { onNavigateToTopicPicker(false) }) { Text("Edit Topics", color = CrimsonAccent, fontWeight = FontWeight.SemiBold) }
                    }
                }
            }

            // ── Settings row ─────────────────────────────────────────────────
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(14.dp)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Text("Show Timer Overlay", style = MaterialTheme.typography.bodyMedium)
                    }
                    Switch(checked = uiState.timerOverlayEnabled, onCheckedChange = { viewModel.setTimerOverlayEnabled(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPass, checkedTrackColor = EmeraldPass.copy(alpha = 0.4f)))
                }
            }

            // ── Debug panel ──────────────────────────────────────────────────
            if (BuildConfig.DEBUG) {
                DebugSessionPanel(onSimulateQuiz = { viewModel.debugSimulateQuiz() }, onSimulateHaunt = { viewModel.debugSimulateHaunt() },
                    onSimulateCooldown = { viewModel.debugSimulateCooldown() }, onInjectUsage = { viewModel.debugInjectUsage() })
            }
            Spacer(Modifier.height(16.dp))
        }
        }
    }
}

@Composable
private fun UsageDonut(uiState: HomeUiState, size: Dp = 240.dp, strokeWidth: Dp = 18.dp) {
    val appColors = listOf(InstagramReelsColor, YouTubeShortsColor, TikTokColor, FacebookReelsColor)
    val appLabels = listOf("Reels", "Shorts", "TikTok", "FB Reels")
    val appUsages = listOf(uiState.reelsDailyUsageMillis, uiState.shortsDailyUsageMillis, uiState.tiktokDailyUsageMillis, uiState.facebookReelsDailyUsageMillis)
    val limitMs = if (uiState.timeLimit > 0) uiState.timeLimit else 900_000L
    val animatedFractions = appUsages.mapIndexed { i, usage ->
        val target = (usage.toFloat() / limitMs).coerceIn(0f, 1f)
        val anim by animateFloatAsState(targetValue = target, animationSpec = tween(900, easing = FastOutSlowInEasing), label = "arc$i")
        anim
    }
    val totalFmt = fmtMs(uiState.totalDailyUsageMillis)
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(size)) {
                val stroke = strokeWidth.toPx()
                val gap = 6f
                val segmentSweep = (360f - gap * 4) / 4f
                val radius = (this.size.minDimension - stroke) / 2f
                val tl = Offset((this.size.width - radius * 2) / 2f, (this.size.height - radius * 2) / 2f)
                val arcSize = Size(radius * 2, radius * 2)
                appColors.forEachIndexed { i, color ->
                    val start = -90f + i * (segmentSweep + gap)
                    // Track
                    drawArc(color = color.copy(alpha = 0.15f), startAngle = start, sweepAngle = segmentSweep,
                        useCenter = false, topLeft = tl, size = arcSize, style = Stroke(stroke, cap = StrokeCap.Round))
                    // Fill
                    if (animatedFractions[i] > 0f)
                        drawArc(color = color, startAngle = start, sweepAngle = segmentSweep * animatedFractions[i],
                            useCenter = false, topLeft = tl, size = arcSize, style = Stroke(stroke, cap = StrokeCap.Round))
                }
            }
            // Center text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(totalFmt, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("today", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
        // Legend row
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            appLabels.forEachIndexed { i, label ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Canvas(Modifier.size(8.dp)) { drawCircle(appColors[i]) }
                    Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(fmtMs(appUsages[i]), style = MaterialTheme.typography.labelSmall, color = TextPrimary, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun ModeCard(label: String, icon: ImageVector, option: BlockOption, current: BlockOption, modifier: Modifier, onSelect: (BlockOption) -> Unit) {
    val selected = current == option
    val bg = if (selected) CrimsonAccent else SurfaceCard
    val fg = if (selected) Color.White else TextSecondary
    Card(modifier = modifier.clickable { onSelect(option) }, colors = CardDefaults.cardColors(containerColor = bg), shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(vertical = 12.dp, horizontal = 6.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(22.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = fg, textAlign = TextAlign.Center, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}

@Composable
private fun StatusPill(text: String, color: Color) {
    Surface(color = color.copy(alpha = 0.15f), shape = CircleShape) {
        Text(text, style = MaterialTheme.typography.labelMedium, color = color, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp))
    }
}

private fun fmtMs(ms: Long): String {
    val s = ms / 1000; val h = s / 3600; val m = (s % 3600) / 60; val sec = s % 60
    return if (h > 0) "${h}h ${m}m" else if (m > 0) "${m}m" else "${sec}s"
}
