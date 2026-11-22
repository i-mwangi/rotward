@file:OptIn(ExperimentalMaterial3Api::class)
package com.hexscroll.app.ui.home.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hexscroll.app.designsystem.theme.CrimsonAccent

@Composable
fun HelpDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("How HexScroll Works", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                HelpItem("Block All", "Opens TikTok/Reels/Shorts? Instant exit every time.")
                HelpItem("Daily Limit", "You get a set number of minutes per day across all blocked apps. Timer counts down — when it hits zero you get kicked out for the rest of the day.")
                HelpItem("Interval Timer", "You get short bursts of access (default 5 min) followed by forced breaks (default 15 min), cycling all day.")
                HelpItem("Learning Gate", "Before any blocked feed opens, you must answer one question from your chosen topics. Answer correctly = 5 min grace window. Answer wrong = haunting screen + 3 min cooldown.")
                HelpItem("Cooldown", "If you try to open a blocked feed while in cooldown, you skip the quiz and get the haunting screen immediately.")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = CrimsonAccent) }
        },
    )
}

@Composable
private fun HelpItem(title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(body, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
