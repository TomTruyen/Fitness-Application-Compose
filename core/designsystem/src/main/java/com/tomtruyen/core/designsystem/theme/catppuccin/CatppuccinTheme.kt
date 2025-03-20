package com.tomtruyen.core.designsystem.theme.catppuccin

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

object CatppuccinTheme {

    @Composable
    fun Palette(
        palette: CatppuccinPalette = Catppuccin.Latte,
        content: @Composable () -> Unit,
    ) {
        val colorScheme = if (palette is CatppuccinMaterial) {
            palette.materialColorScheme()
        } else {
            palette.colorScheme()
        }

        return Custom(
            colorScheme = colorScheme,
            typography = palette.typography(),
            content = content,
        )
    }

    @Composable
    fun Custom(
        colorScheme: ColorScheme = Catppuccin.Latte.colorScheme(),
        typography: Typography = Catppuccin.Latte.typography(),
        shapes: Shapes = MaterialTheme.shapes,
        content: @Composable () -> Unit,
    ) {
        return MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes,
            content = content,
        )
    }
}