package com.tomtruyen.core.designsystem.theme

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.tomtruyen.core.designsystem.theme.datastore.ThemePreferencesDatastore

@Composable
fun rememberDarkMode(): State<Boolean> {
    val themeMode by ThemePreferencesDatastore.themeMode.collectAsState(ThemePreferencesDatastore.Mode.SYSTEM)
    val isSystemInDarkTheme = isSystemInDarkTheme()

    return remember(themeMode) {
        mutableStateOf(
            when (themeMode) {
                ThemePreferencesDatastore.Mode.DARK -> true
                ThemePreferencesDatastore.Mode.LIGHT -> false
                ThemePreferencesDatastore.Mode.SYSTEM -> isSystemInDarkTheme
            }
        )
    }
}

@Composable
fun rememberColorSchemeState(
    lightColor: Color,
    darkColor: Color
): State<Color> {
    val isDarkMode by rememberDarkMode()

    return remember(isDarkMode) {
        mutableStateOf(if (isDarkMode) darkColor else lightColor)
    }
}

// Custom Colors
val ColorScheme.placeholder: State<Color>
    @Composable
    get() = rememberColorSchemeState(
        lightColor = BlueGrey,
        darkColor = ChineseSilver
    )

val ColorScheme.textFieldIcon: State<Color>
    @Composable
    get() = rememberColorSchemeState(
        lightColor = BlueGrey,
        darkColor = ChineseSilver
    )


val ColorScheme.secondaryTextColor: State<Color>
    @Composable
    get() = rememberColorSchemeState(
        lightColor = BlueGrey,
        darkColor = ChineseSilver
    )

val ColorScheme.navigationItemContentColorActive: Color
    @Composable
    get() = background

val ColorScheme.navigationItemContentColorInactive: Color
    @Composable
    get() = ChineseSilver

val ColorScheme.navigationItemBackgroundColorActive: State<Color>
    @Composable
    get() = rememberColorSchemeState(
        lightColor = ChineseBlack,
        darkColor = MauiMist
    )

val ColorScheme.secondaryLabelColor: State<Color>
    @Composable
    get() = rememberColorSchemeState(
        lightColor = DarkGray,
        darkColor = ChineseSilver
    )

val ColorScheme.borderColor: State<Color>
    @Composable
    get() = rememberColorSchemeState(
        lightColor = MauiMist,
        darkColor = DarkGray
    )

val ColorScheme.success: State<Color>
    @Composable
    get() = rememberColorSchemeState(
        lightColor = LighterSuccessGreen,
        darkColor = DarkerSuccessGreen
    )

val ColorScheme.selectedListItem: State<Color>
    @Composable
    get() = rememberColorSchemeState(
        lightColor = DarkGray,
        darkColor = LavenderMist
    )

val ColorScheme.fallbackImageBackground: State<Color>
    @Composable
    get() = rememberColorSchemeState(
        lightColor = ChineseBlack,
        darkColor = ChineseSilver
    )
