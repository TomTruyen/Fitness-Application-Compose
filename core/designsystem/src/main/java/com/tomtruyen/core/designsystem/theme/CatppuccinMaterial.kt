package com.tomtruyen.core.designsystem.theme

import androidx.activity.SystemBarStyle
import androidx.compose.ui.graphics.Color

sealed class CatppuccinMaterial(
    val primaryColor: Color,
    val secondaryColor: Color,
    val tertiaryColor: Color,
    val errorColor: Color,
    palette: CatppuccinPalette
): CatppuccinPalette {

    data class Latte(
        val primary: Color = Catppuccin.Latte.Mauve,
        val secondary: Color = Catppuccin.Latte.Sky,
        val tertiary: Color = Catppuccin.Latte.Sapphire,
        val error: Color = Catppuccin.Latte.Red,
        override val StatusBarStyle: SystemBarStyle = Catppuccin.Latte.StatusBarStyle
    ): CatppuccinMaterial(
        primary,
        secondary,
        tertiary,
        error,
        Catppuccin.Latte,
    )

    data class Mocha(
        val primary: Color = Catppuccin.Mocha.Mauve,
        val secondary: Color = Catppuccin.Mocha.Sky,
        val tertiary: Color = Catppuccin.Mocha.Sapphire,
        val error: Color = Catppuccin.Mocha.Red,
        override val StatusBarStyle: SystemBarStyle = Catppuccin.Mocha.StatusBarStyle
    ): CatppuccinMaterial(
        primary,
        secondary,
        tertiary,
        error,
        Catppuccin.Mocha,
    )

    override val Rosewater: Color = palette.Rosewater
    override val Flamingo: Color = palette.Flamingo
    override val Pink: Color = palette.Pink
    override val Mauve: Color = palette.Mauve
    override val Red: Color = palette.Red
    override val Maroon: Color = palette.Maroon
    override val Peach: Color = palette.Peach
    override val Yellow: Color = palette.Yellow
    override val Green: Color = palette.Green
    override val Teal: Color = palette.Teal
    override val Sky: Color = palette.Sky
    override val Sapphire: Color = palette.Sapphire
    override val Blue: Color = palette.Blue
    override val Lavender: Color = palette.Lavender
    override val Text: Color = palette.Text
    override val Subtext1: Color = palette.Subtext1
    override val Subtext0: Color = palette.Subtext0
    override val Overlay2: Color = palette.Overlay2
    override val Overlay1: Color = palette.Overlay1
    override val Overlay0: Color = palette.Overlay0
    override val Surface2: Color = palette.Surface2
    override val Surface1: Color = palette.Surface1
    override val Surface0: Color = palette.Surface0
    override val Base: Color = palette.Base
    override val Mantle: Color = palette.Mantle
    override val Crust: Color = palette.Crust
}