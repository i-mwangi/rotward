@file:OptIn(ExperimentalMaterial3Api::class)
package com.hexscroll.app.ui.home.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hexscroll.app.designsystem.theme.CrimsonAccent
import com.hexscroll.app.designsystem.theme.EmeraldPass

@Composable
fun IntervalTimerDialog(
    currentAllowanceMillis: Long,
    currentBreakMillis: Long,
    onConfirm: (allowanceMillis: Long, breakMillis: Long) -> Unit,
    onDismiss: () -> Unit,
) {
    var allowanceMins by remember { mutableIntStateOf((currentAllowanceMillis / 60000).toInt().coerceIn(1, 30)) }
    var breakMins by remember { mutableIntStateOf((currentBreakMillis / 60000).toInt().coerceIn(5, 60)) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Interval Timer", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("You get short bursts of access followed by forced breaks, cycling all day.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Usage window", style = MaterialTheme.typography.labelMedium)
                        Text("${allowanceMins} min", fontWeight = FontWeight.Bold, color = EmeraldPass)
                    }
                    Slider(value = allowanceMins.toFloat(), onValueChange = { allowanceMins = it.toInt() },
                        valueRange = 1f..30f, steps = 28,
                        colors = SliderDefaults.colors(thumbColor = EmeraldPass, activeTrackColor = EmeraldPass))
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Break length", style = MaterialTheme.typography.labelMedium)
                        Text("${breakMins} min", fontWeight = FontWeight.Bold, color = CrimsonAccent)
                    }
                    Slider(value = breakMins.toFloat(), onValueChange = { breakMins = it.toInt() },
                        valueRange = 5f..60f, steps = 54,
                        colors = SliderDefaults.colors(thumbColor = CrimsonAccent, activeTrackColor = CrimsonAccent))
                }
                Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small) {
                    Text("${allowanceMins}m on / ${breakMins}m off, repeating all day",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(allowanceMins * 60_000L, breakMins * 60_000L) },
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonAccent)) {
                Text("Set Interval")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
