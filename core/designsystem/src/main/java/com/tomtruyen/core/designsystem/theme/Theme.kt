package com.tomtruyen.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White

private val DarkColorScheme = lightColorScheme(
    primary = Cultured,
    onPrimary = ChineseBlack,
    secondary = ChineseBlack,
    onSecondary = Cultured,
    tertiary = Cultured,
    onTertiary = ChineseBlack,
    background = ChineseBlack,
    onBackground = White,
    primaryContainer = Cultured,
    onPrimaryContainer = ChineseBlack,
    errorContainer = LighterRed,
    onErrorContainer = White,
    surface = DarkGray,
    onSurface = Cultured,
    error = LighterRed,
    outlineVariant = DarkGray, // Divider
)

private val LightColorScheme = lightColorScheme(
    primary = ChineseBlack,
    onPrimary = White,
    secondary = Cultured,
    onSecondary = ChineseBlack,
    tertiary = ChineseBlack,
    onTertiary = Cultured,
    background = White,
    onBackground = ChineseBlack,
    primaryContainer = ChineseBlack,
    onPrimaryContainer = White,
    errorContainer = LighterRed,
    onErrorContainer = White,
    surface = LavenderMist,
    onSurface = ChineseBlack,
    error = LighterRed,
    outlineVariant = LavenderMist, // Divider
)

@Composable
fun FynixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}