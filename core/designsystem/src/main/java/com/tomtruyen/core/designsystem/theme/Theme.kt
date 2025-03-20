package com.tomtruyen.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.tomtruyen.core.common.ThemeMode
import com.tomtruyen.core.common.models.GlobalAppState

@Composable
fun FynixTheme(
    content: @Composable () -> Unit
) {
    val palette by rememberColorPalette()

    CatppuccinTheme.Palette(
        palette = palette,
        content = content
    )
}

@Composable
fun rememberColorPalette(): State<CatppuccinPalette> {
    val theme by GlobalAppState.theme
    val isSystemInDarkTheme = isSystemInDarkTheme()

    return remember {
        derivedStateOf {
            when (theme) {
                ThemeMode.DARK -> CatppuccinMaterial.Mocha()
                ThemeMode.SYSTEM if isSystemInDarkTheme -> CatppuccinMaterial.Mocha()
                else -> CatppuccinMaterial.Latte()
            }
        }
    }
}