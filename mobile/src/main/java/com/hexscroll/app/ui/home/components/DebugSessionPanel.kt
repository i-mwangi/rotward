@file:OptIn(ExperimentalMaterial3Api::class)
package com.hexscroll.app.ui.home.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexscroll.app.designsystem.theme.AmberWarn
import com.hexscroll.app.designsystem.theme.CrimsonAccent
import com.hexscroll.app.designsystem.theme.EmeraldPass
import com.hexscroll.app.designsystem.theme.SurfaceElevated

@Composable
fun DebugSessionPanel(
    onSimulateQuiz: () -> Unit,
    onSimulateHaunt: () -> Unit,
    onSimulateCooldown: () -> Unit,
    onInjectUsage: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(color = AmberWarn.copy(alpha = 0.2f), shape = MaterialTheme.shapes.extraSmall) {
                    Text("DEBUG", style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                        color = AmberWarn, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
                Text("Session Panel", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Text("Simulate overlay flows without opening TikTok/Reels.",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DebugButton("Quiz", EmeraldPass, Modifier.weight(1f), onSimulateQuiz)
                DebugButton("Haunt", CrimsonAccent, Modifier.weight(1f), onSimulateHaunt)
                DebugButton("Cooldown", AmberWarn, Modifier.weight(1f), onSimulateCooldown)
                DebugButton("+5m", Color.Gray, Modifier.weight(1f), onInjectUsage)
            }
        }
    }
}

@Composable
private fun DebugButton(label: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
        border = ButtonDefaults.outlinedButtonBorder,
    ) { Text(label, fontSize = 11.sp, maxLines = 1) }
}
