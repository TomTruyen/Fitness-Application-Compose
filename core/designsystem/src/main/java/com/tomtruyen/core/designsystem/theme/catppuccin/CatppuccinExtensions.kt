package com.tomtruyen.core.designsystem.theme.catppuccin

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import com.tomtruyen.core.designsystem.theme.defaultFontFamily
import com.tomtruyen.core.designsystem.theme.fontFamily

fun Color.inverse(): Color {
    return Color(
        red = 1f - red,
        green = 1f - green,
        blue = 1f - blue,
        alpha = alpha,
    )
}

fun CatppuccinMaterial.materialColorScheme(): ColorScheme {
    return this.colorScheme(
        primary = primaryColor,
        secondary = secondaryColor,
        tertiary = tertiaryColor,
        error = errorColor,
    )
}

fun CatppuccinPalette.colorScheme(
    primary: Color = Mauve,
    onPrimary: Color = Base,
    primaryContainer: Color = Mauve,
    onPrimaryContainer: Color = Base,
    inversePrimary: Color = primary.inverse(),
    secondary: Color = Sapphire,
    onSecondary: Color = Mantle,
    secondaryContainer: Color = secondary,
    onSecondaryContainer: Color = Mantle,
    tertiary: Color = Sky,
    onTertiary: Color = Base,
    tertiaryContainer: Color = Sky,
    onTertiaryContainer: Color = Base,
    background: Color = Base,
    onBackground: Color = Text,
    surface: Color = Mantle,
    onSurface: Color = Text,
    surfaceVariant: Color = Mantle,
    onSurfaceVariant: Color = surfaceVariant.inverse(),
    surfaceTint: Color = Crust,
    inverseSurface: Color = surface.inverse(),
    inverseOnSurface: Color = onSurface.inverse(),
    error: Color = Red,
    onError: Color = Base,
    errorContainer: Color = Rosewater,
    onErrorContainer: Color = Rosewater.inverse(),
    outline: Color = primary,
    outlineVariant: Color = surface,
    scrim: Color = Crust,
): ColorScheme {
    return ColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        // TODO: Check if we need other valuse for these
        surfaceBright = surface,
        surfaceDim = surface,
        surfaceContainer = surface,
        surfaceContainerHigh = surface,
        surfaceContainerHighest = surface,
        surfaceContainerLow = surface,
        surfaceContainerLowest = surface
    )
}

fun CatppuccinPalette.typography(): Typography {
    return Typography(
        displayLarge = Typography().displayLarge.copy(
            color = Text,
        ),
        displayMedium = Typography().displayMedium.copy(
            color = Text,
        ),
        displaySmall = Typography().displaySmall.copy(
            color = Text,
        ),
        headlineLarge = Typography().headlineLarge.copy(
            color = Text,
        ),
        headlineMedium = Typography().headlineMedium.copy(
            color = Text,
        ),
        headlineSmall  = Typography().headlineSmall.copy(
            color = Text,
        ),
        titleLarge = Typography().titleLarge.copy(
            color = Text,
        ),
        titleMedium = Typography().titleMedium.copy(
            color = Text,
        ),
        titleSmall = Typography().titleSmall.copy(
            color = Text,
        ),
        bodyLarge = Typography().bodyLarge.copy(
            color = Text,
        ),
        bodyMedium = Typography().bodyMedium.copy(
            color = Text,
        ),
        bodySmall = Typography().bodySmall.copy(
            color = Text,
        ),
        labelLarge = Typography().labelLarge.copy(
            color = Overlay1,
        ),
        labelMedium = Typography().labelMedium.copy(
            color = Overlay1,
        ),
        labelSmall = Typography().labelSmall.copy(
            color = Overlay1,
        ),
    ).defaultFontFamily(fontFamily)
}