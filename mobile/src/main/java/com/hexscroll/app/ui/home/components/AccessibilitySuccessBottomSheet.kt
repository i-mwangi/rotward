@file:OptIn(ExperimentalMaterial3Api::class)
package com.hexscroll.app.ui.home.components
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hexscroll.app.designsystem.theme.EmeraldPass

@Composable
fun AccessibilitySuccessBottomSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPass, modifier = Modifier.size(56.dp))
            Text("HexScroll is Active", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("The accessibility service is running. HexScroll will now monitor and block short-form video feeds according to your settings.",
                style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPass)) {
                Text("Got it", color = MaterialTheme.colorScheme.background)
            }
        }
    }
}
