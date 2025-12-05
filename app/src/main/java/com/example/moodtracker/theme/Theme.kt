package com.example.moodtracker.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = AppPrimary,
    onPrimary = Color.White,
    secondary = AppSecondary,
    onSecondary = Color.Black,
    background = LightBackground,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    outline = NeutralColor
)

@Composable
fun MoodTrackerTheme(
    content: @Composable () -> Unit
) {
    // FORCE LIGHT THEME — ALWAYS
    val colors = LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
