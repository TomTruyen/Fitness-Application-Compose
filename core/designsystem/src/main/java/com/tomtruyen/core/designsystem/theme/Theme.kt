package com.tomtruyen.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White

private val DarkColorScheme = lightColorScheme(
    primary = ChineseBlack,
    onPrimary = White,
    secondary = Cultured,
    onSecondary = ChineseBlack,
    tertiary = ChineseBlack,
    onTertiary = White,
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

private val LightColorScheme = lightColorScheme(
    primary = ChineseBlack,
    onPrimary = White,
    secondary = Cultured,
    onSecondary = ChineseBlack,
    tertiary = ChineseBlack,
    onTertiary = ChineseBlack,
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