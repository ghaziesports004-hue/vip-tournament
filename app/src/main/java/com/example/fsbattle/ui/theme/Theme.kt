package com.example.fsbattle.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    secondary = CyanHighlight,
    tertiary = GoldenCoin,
    background = Slate900,
    surface = Slate800,
    surfaceVariant = Slate700,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Slate100,
    onSurface = Slate100,
    onSurfaceVariant = Slate600
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    secondary = CyanHighlight,
    tertiary = GoldenCoin,
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    surfaceVariant = Color(0xFFE2E8F0),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Slate900,
    onSurface = Slate900,
    onSurfaceVariant = Slate600
)

@Composable
fun FsBattleTheme(
    darkTheme: Boolean = true, // Default to Dark Esports theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
