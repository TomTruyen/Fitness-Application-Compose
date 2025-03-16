package com.tomtruyen.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color.Companion.White
import com.tomtruyen.core.common.ThemeMode
import com.tomtruyen.core.common.models.GlobalAppState

private val DarkColorScheme = lightColorScheme(
    primary = MauiMist,
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
    surfaceContainerHigh = MauiMist, // PullRefresh Indicator
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
    surface = MauiMist,
    onSurface = ChineseBlack,
    surfaceContainerHigh = MauiMist, // PullRefresh Indicator
    error = LighterRed,
    outlineVariant = MauiMist, // Divider
)

@Composable
fun FynixTheme(
    content: @Composable () -> Unit
) {
    val theme by GlobalAppState.theme
    val systemDarkMode = isSystemInDarkTheme()

    val colorScheme by remember {
        derivedStateOf {
            when (theme) {
                ThemeMode.DARK -> DarkColorScheme
                ThemeMode.SYSTEM if systemDarkMode -> DarkColorScheme
                else -> LightColorScheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun rememberDarkMode(): State<Boolean> {
    val theme by GlobalAppState.theme
    val isSystemInDarkTheme = isSystemInDarkTheme()

    return remember {
        derivedStateOf {
            when (theme) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme
            }
        }
    }
}

