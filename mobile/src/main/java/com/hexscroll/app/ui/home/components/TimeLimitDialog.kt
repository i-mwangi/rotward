@file:OptIn(ExperimentalMaterial3Api::class)
package com.hexscroll.app.ui.home.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hexscroll.app.designsystem.theme.CrimsonAccent

private val PRESET_MINUTES = listOf(5, 10, 15, 20, 30, 45, 60, 90, 120)

@Composable
fun TimeLimitDialog(currentMillis: Long, onConfirm: (Long) -> Unit, onDismiss: () -> Unit) {
    var selectedMinutes by remember { mutableIntStateOf((currentMillis / 60000).toInt().coerceIn(5, 120)) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Daily Limit", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("How many minutes per day across all blocked apps?",
                    style = MaterialTheme.typography.bodyMedium)
                Text("${selectedMinutes} min", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold, color = CrimsonAccent)
                Slider(
                    value = selectedMinutes.toFloat(),
                    onValueChange = { selectedMinutes = it.toInt() },
                    valueRange = 5f..120f,
                    steps = 22,
                    colors = SliderDefaults.colors(thumbColor = CrimsonAccent, activeTrackColor = CrimsonAccent)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PRESET_MINUTES.forEach { mins ->
                        FilterChip(
                            selected = selectedMinutes == mins,
                            onClick = { selectedMinutes = mins },
                            label = { Text("${mins}m") },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedMinutes * 60_000L) },
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonAccent)) {
                Text("Set Limit")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
