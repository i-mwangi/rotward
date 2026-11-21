package com.hexscroll.app.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.bottomFadeScrim(color: Color = Color.Black, fraction: Float = 0.3f): Modifier =
    drawWithContent {
        drawContent()
        val gradient = Brush.verticalGradient(
            colors = listOf(Color.Transparent, color),
            startY = size.height * (1f - fraction),
            endY = size.height,
        )
        drawRect(brush = gradient)
    }
