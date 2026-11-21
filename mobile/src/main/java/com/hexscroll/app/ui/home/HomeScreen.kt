@file:OptIn(ExperimentalMaterial3Api::class)
package com.hexscroll.app.ui.home
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hexscroll.app.core.model.BlockOption
import com.hexscroll.app.designsystem.component.AnimatedButton
import com.hexscroll.app.designsystem.theme.CrimsonAccent
import com.hexscroll.app.designsystem.theme.EmeraldPass
import com.hexscroll.app.util.openAccessibilitySettings
@Composable
fun HomeScreen(
    onNavigateToTopicPicker: (Boolean) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(uiState.hasLoadedSettings) {
        if (uiState.hasLoadedSettings && !uiState.hasCompletedTopicOnboarding) {
            onNavigateToTopicPicker(true)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HexScroll", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (!uiState.isAccessibilityServiceActive) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Accessibility Service Required", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.error)
                        Text("HexScroll needs accessibility permission to monitor feeds.", style = MaterialTheme.typography.bodySmall)
                        AnimatedButton(text = "Enable Now", onClick = { context.openAccessibilitySettings() }, containerColor = CrimsonAccent)
                    }
                }
            }
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Learning Gate", style = MaterialTheme.typography.titleMedium)
                            Text("Answer a question before scrolling", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(checked = uiState.learningGateEnabled, onCheckedChange = { viewModel.setLearningGateEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPass, checkedTrackColor = EmeraldPass.copy(alpha = 0.5f)))
                    }
                    if (uiState.selectedTopics.isNotEmpty()) {
                        Text(" topics active", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    AnimatedButton(text = "Edit Topics ( selected)", onClick = { onNavigateToTopicPicker(false) },
                        containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface)
                }
            }
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Blocking Mode", style = MaterialTheme.typography.titleMedium)
                    BlockOption.entries.forEach { option ->
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(option.name, style = MaterialTheme.typography.bodyMedium)
                            RadioButton(selected = uiState.blockOption == option, onClick = { viewModel.setBlockOption(option) },
                                colors = RadioButtonDefaults.colors(selectedColor = CrimsonAccent))
                        }
                    }
                }
            }
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Show Timer Overlay", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = uiState.timerOverlayEnabled, onCheckedChange = { viewModel.setTimerOverlayEnabled(it) })
                }
            }
        }
    }
}