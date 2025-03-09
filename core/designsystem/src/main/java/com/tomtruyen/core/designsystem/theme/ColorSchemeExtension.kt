package com.tomtruyen.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.tomtruyen.core.designsystem.theme.datastore.ThemePreferencesDatastore
import org.koin.compose.koinInject

// Convenience
private val isDarkTheme: Boolean
    @Composable
    get() {
        val themePreferences = koinInject<ThemePreferencesDatastore>()
        val themeMode by themePreferences.themeMode.collectAsState(ThemePreferencesDatastore.Mode.SYSTEM)

        return when(themeMode) {
            ThemePreferencesDatastore.Mode.DARK -> true
            ThemePreferencesDatastore.Mode.LIGHT -> false
            ThemePreferencesDatastore.Mode.SYSTEM -> isSystemInDarkTheme()
        }
    }

// Custom Colors
val ColorScheme.placeholder: Color
    @Composable
    get() = if(isDarkTheme) ChineseSilver else BlueGrey

val ColorScheme.textFieldIcon
    @Composable
    get() = if(isDarkTheme) ChineseSilver else BlueGrey

val ColorScheme.textButtonContentColor: Color
    @Composable
    get() = if(isDarkTheme) ChineseSilver else BlueGrey

val ColorScheme.secondaryTextColor: Color
    @Composable
    get() = if(isDarkTheme) ChineseSilver else BlueGrey

val ColorScheme.navigationItemContentColorActive: Color
    @Composable
    get() = background

val ColorScheme.navigationItemContentColorInactive: Color
    @Composable
    get() = ChineseSilver

val ColorScheme.navigationItemBackgroundColorActive: Color
    @Composable
    get() = if(isDarkTheme) Cultured else ChineseBlack

val ColorScheme.secondaryLabelColor: Color
    @Composable
    get() = if(isDarkTheme) ChineseSilver else DarkGray

val ColorScheme.borderColor: Color
    @Composable
    get() = if(isDarkTheme) DarkGray else LavenderMist

val ColorScheme.success: Color
    @Composable
    get() = if(isDarkTheme) DarkerSuccessGreen else LighterSuccessGreen