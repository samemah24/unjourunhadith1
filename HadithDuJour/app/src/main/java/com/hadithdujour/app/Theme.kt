package com.hadithdujour.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1B6B4F),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD3EBDD),
    onPrimaryContainer = Color(0xFF0B2A1E),
    secondary = Color(0xFF4D6358),
    background = Color(0xFFFBFDF9),
    onBackground = Color(0xFF191C1A),
    surface = Color(0xFFFBFDF9),
    onSurface = Color(0xFF191C1A),
    surfaceVariant = Color(0xFFDCE5DD),
    onSurfaceVariant = Color(0xFF414942)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7BD6A5),
    onPrimary = Color(0xFF003824),
    primaryContainer = Color(0xFF1F3D30),
    onPrimaryContainer = Color(0xFFCDEBDB),
    secondary = Color(0xFFB3CCBF),
    background = Color(0xFF101411),
    onBackground = Color(0xFFE1E3DF),
    surface = Color(0xFF101411),
    onSurface = Color(0xFFE1E3DF),
    surfaceVariant = Color(0xFF414942),
    onSurfaceVariant = Color(0xFFC0C9C0)
)

@Composable
fun HadithTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}
