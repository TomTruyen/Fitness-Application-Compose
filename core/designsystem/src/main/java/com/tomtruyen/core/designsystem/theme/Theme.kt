package com.tomtruyen.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color.Companion.White
import com.tomtruyen.core.designsystem.theme.datastore.ThemePreferencesDatastore

private val DarkColorScheme = lightColorScheme(
    primary = LavenderMist,
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
    content: @Composable () -> Unit
) {
    val themeMode by ThemePreferencesDatastore.themeMode.collectAsState(ThemePreferencesDatastore.Mode.SYSTEM)

    val colorScheme = when(themeMode) {
        ThemePreferencesDatastore.Mode.DARK -> DarkColorScheme
        ThemePreferencesDatastore.Mode.SYSTEM if isSystemInDarkTheme() -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}