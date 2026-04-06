package com.example.vibramobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1DB954),
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212),
    surface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFF242424),
    onPrimary = Color.Black,
    onBackground = Color(0xFFF5F5F5),
    onSurface = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFFBDBDBD)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1A8F46),
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    surfaceVariant = Color(0xFFECECEC),
    onPrimary = Color.White,
    onBackground = Color(0xFF111111),
    onSurface = Color(0xFF111111),
    onSurfaceVariant = Color(0xFF555555)
)

@Composable
fun VibraMobileTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}