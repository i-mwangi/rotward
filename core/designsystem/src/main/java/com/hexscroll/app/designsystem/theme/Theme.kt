package com.hexscroll.app.designsystem.theme
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

private val HexScrollDarkColorScheme = darkColorScheme(
    primary                = CrimsonAccent,
    onPrimary              = BoneWhite,
    primaryContainer       = DeepCrimson,
    onPrimaryContainer     = TextPrimary,
    secondary              = EmeraldPass,
    onSecondary            = BackgroundDark,
    secondaryContainer     = EmeraldContainer,
    onSecondaryContainer   = EmeraldPass,
    tertiary               = AmberWarn,
    onTertiary             = BackgroundDark,
    background             = BackgroundDark,
    onBackground           = OnBackgroundDark,
    surface                = SurfaceDark,
    onSurface              = OnSurfaceDark,
    surfaceVariant         = SurfaceContainerHighDark,
    onSurfaceVariant       = OnSurfaceVariantDark,
    surfaceContainer       = SurfaceContainerDark,
    surfaceContainerHigh   = SurfaceContainerHighDark,
    surfaceContainerHighest= SurfaceContainerHighestDark,
    outline                = OutlineDark,
    outlineVariant         = OutlineVariantDark,
    error                  = CrimsonAccent,
    onError                = BoneWhite,
)

private val HexScrollLightColorScheme = lightColorScheme(
    primary                = CrimsonAccentLight,
    onPrimary              = BackgroundLight,
    primaryContainer       = CrimsonAccentLight,
    onPrimaryContainer     = BackgroundLight,
    secondary              = EmeraldPass,
    onSecondary            = BackgroundLight,
    background             = BackgroundLight,
    onBackground           = OnBackgroundLight,
    surface                = SurfaceLight,
    onSurface              = OnSurfaceLight,
    surfaceVariant         = SurfaceContainerHighLight,
    onSurfaceVariant       = OnSurfaceVariantLight,
    surfaceContainer       = SurfaceContainerLight,
    surfaceContainerHigh   = SurfaceContainerHighLight,
    outline                = OutlineLight,
    error                  = CrimsonAccentLight,
    onError                = BackgroundLight,
)

@Composable
fun HexScrollTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    hauntColors: HauntColors = HauntColors(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> HexScrollDarkColorScheme
        else -> HexScrollLightColorScheme
    }
    CompositionLocalProvider(LocalHauntColors provides hauntColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = HexScrollTypography,
            shapes      = HexScrollShapes,
            content     = content,
        )
    }
}

object HexScrollThemeTokens {
    val hauntColors: HauntColors
        @Composable get() = LocalHauntColors.current
}
