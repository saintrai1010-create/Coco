package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GlassColorScheme = darkColorScheme(
    primary = GlassAccentBlue,
    secondary = GlassAccentPurple,
    tertiary = GlassAccentGreen,
    background = GlassBg,
    surface = GlassSurface,
    surfaceVariant = GlassSurfaceVariant,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = TextUltraLight,
    onSurface = TextUltraLight,
    onSurfaceVariant = TextLightSlate,
    error = GlassAccentRed,
    onError = Color.Black
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GlassColorScheme,
        typography = Typography,
        content = content
    )
}
