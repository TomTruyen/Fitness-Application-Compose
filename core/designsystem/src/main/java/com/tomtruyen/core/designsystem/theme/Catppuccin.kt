package com.tomtruyen.core.designsystem.theme

import androidx.activity.SystemBarStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object Catppuccin {

    object Latte : CatppuccinPalette {
        override val Rosewater: Color = Color(0xffdc8a78)
        override val Flamingo: Color = Color(0xffdd7878)
        override val Pink: Color = Color(0xffea76cb)
        override val Mauve: Color = Color(0xff8839ef)
        override val Red: Color = Color(0xffd20f39)
        override val Maroon: Color = Color(0xffe64553)
        override val Peach: Color = Color(0xfffe640b)
        override val Yellow: Color = Color(0xffdf8e1d)
        override val Green: Color = Color(0xff40a02b)
        override val Teal: Color = Color(0xff179299)
        override val Sky: Color = Color(0xff04a5e5)
        override val Sapphire: Color = Color(0xff209fb5)
        override val Blue: Color = Color(0xff1e66f5)
        override val Lavender: Color = Color(0xff7287fd)
        override val Text: Color = Color(0xff4c4f69)
        override val Subtext1: Color = Color(0xff5c5f77)
        override val Subtext0: Color = Color(0xff6c6f85)
        override val Overlay2: Color = Color(0xff7c7f93)
        override val Overlay1: Color = Color(0xff8c8fa1)
        override val Overlay0: Color = Color(0xff9ca0b0)
        override val Surface2: Color = Color(0xffacb0be)
        override val Surface1: Color = Color(0xffbcc0cc)
        override val Surface0: Color = Color(0xffccd0da)
        override val Base: Color = Color(0xffeff1f5)
        override val Mantle: Color = Color(0xffe6e9ef)
        override val Crust: Color = Color(0xffdce0e8)
        override val StatusBarStyle: SystemBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
    }

    object Mocha : CatppuccinPalette {
        override val Rosewater: Color = Color(0xfff5e0dc)
        override val Flamingo: Color = Color(0xfff2cdcd)
        override val Pink: Color = Color(0xfff5c2e7)
        override val Mauve: Color = Color(0xffcba6f7)
        override val Red: Color = Color(0xfff38ba8)
        override val Maroon: Color = Color(0xffeba0ac)
        override val Peach: Color = Color(0xfffab387)
        override val Yellow: Color = Color(0xfff9e2af)
        override val Green: Color = Color(0xffa6e3a1)
        override val Teal: Color = Color(0xff94e2d5)
        override val Sky: Color = Color(0xff89dceb)
        override val Sapphire: Color = Color(0xff74c7ec)
        override val Blue: Color = Color(0xff89b4fa)
        override val Lavender: Color = Color(0xffb4befe)
        override val Text: Color = Color(0xffcdd6f4)
        override val Subtext1: Color = Color(0xffbac2de)
        override val Subtext0: Color = Color(0xffa6adc8)
        override val Overlay2: Color = Color(0xff9399b2)
        override val Overlay1: Color = Color(0xff7f849c)
        override val Overlay0: Color = Color(0xff6c7086)
        override val Surface2: Color = Color(0xff585b70)
        override val Surface1: Color = Color(0xff45475a)
        override val Surface0: Color = Color(0xff313244)
        override val Base: Color = Color(0xff1e1e2e)
        override val Mantle: Color = Color(0xff181825)
        override val Crust: Color = Color(0xff11111b)
        override val StatusBarStyle: SystemBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb())
    }
}

@Suppress("PropertyName")
interface CatppuccinPalette {
    val Rosewater: Color
    val Flamingo: Color
    val Pink: Color
    val Mauve: Color
    val Red: Color
    val Maroon: Color
    val Peach: Color
    val Yellow: Color
    val Green: Color
    val Teal: Color
    val Sky: Color
    val Sapphire: Color
    val Blue: Color
    val Lavender: Color
    val Text: Color
    val Subtext1: Color
    val Subtext0: Color
    val Overlay2: Color
    val Overlay1: Color
    val Overlay0: Color
    val Surface2: Color
    val Surface1: Color
    val Surface0: Color
    val Base: Color
    val Mantle: Color
    val Crust: Color
    val StatusBarStyle: SystemBarStyle
}