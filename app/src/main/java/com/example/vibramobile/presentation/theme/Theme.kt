package com.example.vibramobile.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

private val BaseDarkColorScheme = darkColorScheme(
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

private val BaseLightColorScheme = lightColorScheme(
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
    accentColorHex: String = DEFAULT_ACCENT_COLOR_HEX,
    content: @Composable () -> Unit
) {
    val accentColor = accentColorFromHex(accentColorHex)
    val onAccentColor = if (accentColor.luminance() > 0.55f) Color.Black else Color.White
    val colorScheme = if (darkTheme) {
        BaseDarkColorScheme.copy(
            primary = accentColor,
            primaryContainer = accentColor.copy(alpha = 0.24f),
            onPrimary = onAccentColor,
            onPrimaryContainer = BaseDarkColorScheme.onBackground
        )
    } else {
        BaseLightColorScheme.copy(
            primary = accentColor,
            primaryContainer = accentColor.copy(alpha = 0.16f),
            onPrimary = onAccentColor,
            onPrimaryContainer = BaseLightColorScheme.onBackground
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private fun accentColorFromHex(hex: String): Color {
    val normalized = hex.trim().uppercase()
    val safeHex = if (normalized in AccentColorHexList) normalized else DEFAULT_ACCENT_COLOR_HEX
    return Color(android.graphics.Color.parseColor(safeHex))
}

