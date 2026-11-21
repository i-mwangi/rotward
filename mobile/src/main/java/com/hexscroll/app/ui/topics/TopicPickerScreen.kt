@file:OptIn(ExperimentalMaterial3Api::class)
package com.hexscroll.app.ui.topics
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hexscroll.app.core.model.LearningTopic
import com.hexscroll.app.designsystem.theme.CrimsonAccent
import com.hexscroll.app.designsystem.theme.EmeraldPass
@Composable
fun TopicPickerScreen(
    isOnboarding: Boolean,
    onConfirm: () -> Unit,
    onBack: () -> Unit,
    viewModel: TopicPickerViewModel = hiltViewModel(),
) {
    val selected by viewModel.selected.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showMaxSnackbar by remember { mutableStateOf(false) }
    LaunchedEffect(showMaxSnackbar) {
        if (showMaxSnackbar) { snackbarHostState.showSnackbar("Maximum  topics"); showMaxSnackbar = false }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (isOnboarding) "Pick Your Topics" else "Edit Topics") },
                navigationIcon = { if (!isOnboarding) IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        bottomBar = {
            Column(Modifier.padding(16.dp)) {
                Text("Pick – topics ( selected)",
                    style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
                Button(onClick = { viewModel.confirm(onConfirm) }, enabled = viewModel.isValid(), modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonAccent)) { Text("Confirm") }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(padding)) {
            items(viewModel.allTopics) { topic ->
                val isSelected = selected.contains(topic)
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .border(if (isSelected) 2.dp else 0.dp, if (isSelected) EmeraldPass else MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
                        .clickable { if (isSelected) viewModel.toggle(topic) else if (viewModel.canAddMore()) viewModel.toggle(topic) else showMaxSnackbar = true },
                    colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface),
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(topic.displayName, style = MaterialTheme.typography.titleSmall)
                            if (isSelected) Icon(Icons.Default.Check, null, tint = EmeraldPass, modifier = Modifier.size(16.dp))
                        }
                        Text(topic.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}