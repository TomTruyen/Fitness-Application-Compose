package com.tomtruyen.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Convenience
private val isDarkTheme: Boolean
    @Composable
    get() = isSystemInDarkTheme()

// Custom Colors
val ColorScheme.placeholder: Color
    @Composable
    get() = BlueGrey

val ColorScheme.textFieldIcon
    @Composable
    get() = BlueGrey

val ColorScheme.textButtonContentColor: Color
    @Composable
    get() = BlueGrey

val ColorScheme.secondaryTextColor: Color
    @Composable
    get() = BlueGrey

val ColorScheme.navigationItemContentColorActive: Color
    @Composable
    get() = background

val ColorScheme.navigationItemContentColorInactive: Color
    @Composable
    get() = ChineseSilver

val ColorScheme.navigationItemBackgroundColorActive: Color
    @Composable
    get() = ChineseBlack