package com.hexscroll.app.designsystem.theme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
data class HauntColors(
    val background: Color = HauntBackground,
    val surface: Color = HauntSurface,
    val textPrimary: Color = BoneWhite,
    val textAccent: Color = PhosphorRed,
    val textGlitchCyan: Color = CyanGlitch,
    val textGlitchCrimson: Color = CrimsonAccent,
    val scanlineColor: Color = Color(0x0AFFFFFF),
    val vignetteColor: Color = Color(0xCC000000),
)
val LocalHauntColors = staticCompositionLocalOf { HauntColors() }
