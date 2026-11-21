@file:OptIn(ExperimentalMaterial3Api::class)
package com.hexscroll.app.ui.overlay

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexscroll.app.designsystem.component.CrtScanlineOverlay
import com.hexscroll.app.designsystem.component.GlitchOffsetText
import com.hexscroll.app.designsystem.component.ScreenFlickerBox
import com.hexscroll.app.designsystem.theme.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import android.os.Build
import kotlinx.coroutines.launch
@Composable
fun ChallengeOverlayContent(state: ChallengeOverlayState, onAnswerSelected: (Int) -> Unit, onHauntComplete: () -> Unit, modifier: Modifier = Modifier) {
    AnimatedContent(targetState = state, label = "overlayState", modifier = modifier) { s ->
        when (s) {
            is ChallengeOverlayState.Quiz -> QuizContent(state = s, onAnswerSelected = onAnswerSelected)
            is ChallengeOverlayState.Haunting -> HauntContent(state = s, onComplete = onHauntComplete, context = androidx.compose.ui.platform.LocalContext.current)
        }
    }
}

private enum class AnswerResult { NONE, CORRECT, WRONG }

@Composable
private fun QuizContent(state: ChallengeOverlayState.Quiz, onAnswerSelected: (Int) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var answerResult by remember { mutableStateOf(AnswerResult.NONE) }
    var timeLeft by remember { mutableIntStateOf(state.timeLimitSeconds) }
    var locked by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (timeLeft > 0 && !locked) { delay(1000); timeLeft-- }
        if (!locked) {
            locked = true; answerResult = AnswerResult.WRONG; feedbackMessage = "Time is up!"
            delay(700); onAnswerSelected(-1)
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(ObsidianCard)) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Surface(color = CrimsonAccent.copy(alpha = 0.2f), shape = RoundedCornerShape(50)) {
                    Text(text = state.question.topic.displayName, style = MaterialTheme.typography.labelSmall, color = CrimsonAccent, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
                Text(text = "${timeLeft}s", style = MaterialTheme.typography.labelLarge, color = if (timeLeft <= 5) CrimsonAccent else TextSecondary, fontWeight = FontWeight.Bold)
            }
            val progress by animateFloatAsState(targetValue = timeLeft.toFloat() / state.timeLimitSeconds, animationSpec = tween(500), label = "timerBar")
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)), color = if (timeLeft <= 5) CrimsonAccent else EmeraldPass, trackColor = SurfaceCard)
            if (feedbackMessage.isNotEmpty()) {
                val bc = if (answerResult == AnswerResult.CORRECT) EmeraldPass else CrimsonAccent
                Surface(modifier = Modifier.fillMaxWidth(), color = bc.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                    Text(text = feedbackMessage, style = MaterialTheme.typography.labelLarge, color = bc, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(10.dp))
                }
            }
            Text(text = state.question.prompt, style = MaterialTheme.typography.titleMedium, color = TextPrimary, lineHeight = 28.sp)
            state.question.options.forEachIndexed { index, option ->
                val bgColor = when { answerResult == AnswerResult.NONE -> OptionUnselected; index == state.question.correctIndex -> OptionSelected; index == selectedIndex -> OptionWrong; else -> OptionUnselected }
                val tc = when { answerResult != AnswerResult.NONE && index == state.question.correctIndex -> EmeraldPass; answerResult != AnswerResult.NONE && index == selectedIndex -> CrimsonAccent; else -> TextPrimary }
                Surface(onClick = { if (!locked) { locked = true; selectedIndex = index; val ok = index == state.question.correctIndex; answerResult = if (ok) AnswerResult.CORRECT else AnswerResult.WRONG; feedbackMessage = if (ok) "Correct! +5 min unlocked" else "Wrong answer"; GlobalScope.launch(Dispatchers.Main) { delay(700); onAnswerSelected(index) } } }, modifier = Modifier.fillMaxWidth(), color = bgColor, shape = RoundedCornerShape(10.dp)) {
                    Text(text = option, style = MaterialTheme.typography.bodyMedium, color = tc, modifier = Modifier.padding(14.dp))
                }
            }
        }
    }
}

@Composable
private fun HauntContent(state: ChallengeOverlayState.Haunting, onComplete: () -> Unit, context: android.content.Context = androidx.compose.ui.platform.LocalContext.current) {
    var countdown by remember { mutableIntStateOf(state.lockoutSeconds) }
    val randomImage = remember { HauntAssets.randomImage() }
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
                else add(GifDecoder.Factory())
            }.build()
    }
    LaunchedEffect(Unit) {
        playHauntSound(context, HauntAssets.randomSound())
        while (countdown > 0) { delay(1000); countdown-- }
        onComplete()
    }
    ScreenFlickerBox(backgroundColor = HauntBackground) {
        Box(modifier = Modifier.fillMaxSize()) {
            CrtScanlineOverlay(modifier = Modifier.fillMaxSize())
            Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                GlitchOffsetText(text = state.headline, style = androidx.compose.ui.text.TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(24.dp))
                AsyncImage(
                    model = ImageRequest.Builder(context).data("file:///android_asset/$randomImage").build(),
                    imageLoader = imageLoader,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                )
                Spacer(Modifier.height(16.dp))
                if (state.correctAnswer != null) { Text("Correct answer:", style = MaterialTheme.typography.labelMedium, color = PhosphorGreen, textAlign = TextAlign.Center); Spacer(Modifier.height(4.dp)); Text(state.correctAnswer, style = MaterialTheme.typography.bodyLarge, color = BoneWhite, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center) }
                if (state.explanation != null) { Spacer(Modifier.height(12.dp)); Text(state.explanation, style = MaterialTheme.typography.bodySmall, color = TextSecondary, textAlign = TextAlign.Center) }
                state.cooldownRemainingMillis?.let { ms -> Spacer(Modifier.height(16.dp)); val m = (ms/60000).toInt(); val s = ((ms%60000)/1000).toInt(); Text("Locked for %02d:%02d".format(m,s), style = MaterialTheme.typography.labelLarge, color = PhosphorRed, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.height(32.dp))
                Text("${state.subtext} ($countdown)", style = MaterialTheme.typography.labelMedium, color = TextDisabled, textAlign = TextAlign.Center)
            }
        }
    }
}
