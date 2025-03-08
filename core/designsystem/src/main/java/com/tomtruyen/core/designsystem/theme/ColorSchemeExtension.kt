package com.tomtruyen.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Convenience
private val isDarkTheme: Boolean
    @Composable
    get() = isSystemInDarkTheme()

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