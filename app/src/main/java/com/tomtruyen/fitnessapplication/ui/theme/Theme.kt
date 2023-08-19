package com.tomtruyen.fitnessapplication.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = lightColorScheme(
    primary = SoftBlue,
    onPrimary = White,
    secondary = CharcoalBlack,
    onSecondary = ChineseWhite,
    tertiary = JordyBlue,
    onTertiary = White,
    background = CharcoalBlack,
    onBackground = ChineseWhite,
    primaryContainer = JordyBlue,
    onPrimaryContainer = ChineseBlack,
    errorContainer = Firebrick,
    onErrorContainer = ChineseBlack,
    surface = ChineseBlack,
    onSurface = ChineseWhite,
    error = Firebrick
)

private val LightColorScheme = lightColorScheme(
    primary = SoftBlue,
    onPrimary = White,
    secondary = ChineseWhite,
    onSecondary = CharcoalBlack,
    tertiary = JordyBlue,
    onTertiary = White,
    background = White,
    onBackground = ChineseBlack,
    primaryContainer = JordyBlue,
    onPrimaryContainer = White,
    errorContainer = Firebrick,
    onErrorContainer = White,
    surface = ChineseWhite,
    onSurface = ChineseBlack,
    error = Firebrick,
)

@Composable
fun FitnessApplicationTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        // Dynamic color is available on Android 12+
        content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        val currentWindow = (view.context as Activity).window

        SideEffect {
            currentWindow.statusBarColor = colorScheme.background.toArgb()
            currentWindow.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}