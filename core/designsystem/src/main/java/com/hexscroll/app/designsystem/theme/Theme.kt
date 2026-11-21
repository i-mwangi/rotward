package com.hexscroll.app.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val HexScrollDarkColorScheme = darkColorScheme(
    primary = CrimsonAccent,
    onPrimary = BoneWhite,
    primaryContainer = DeepCrimson,
    onPrimaryContainer = TextPrimary,
    secondary = EmeraldPass,
    onSecondary = Background,
    secondaryContainer = EmeraldContainer,
    onSecondaryContainer = EmeraldPass,
    tertiary = AmberWarn,
    onTertiary = Background,
    background = Background,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = OutlineSubtle,
    error = CrimsonAccent,
    onError = BoneWhite,
)

@Composable
fun HexScrollTheme(
    hauntColors: HauntColors = HauntColors(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalHauntColors provides hauntColors) {
        MaterialTheme(
            colorScheme = HexScrollDarkColorScheme,
            typography = HexScrollTypography,
            shapes = HexScrollShapes,
            content = content,
        )
    }
}

// Convenience accessor
object HexScrollThemeTokens {
    val hauntColors: HauntColors
        @Composable get() = LocalHauntColors.current
}
