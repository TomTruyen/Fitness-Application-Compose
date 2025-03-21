package com.tomtruyen.feature.settings.remember

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tomtruyen.core.common.ThemeMode
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.feature.settings.SettingsUiAction

@Composable
fun rememberThemeModeActions(
    onAction: (SettingsUiAction) -> Unit
): List<BottomSheetItem> {
    return remember {
        ThemeMode.entries.map { mode ->
            BottomSheetItem(
                title = mode.value,
                icon = when (mode) {
                    ThemeMode.DARK -> Icons.Rounded.DarkMode
                    ThemeMode.LIGHT -> Icons.Rounded.LightMode
                    ThemeMode.SYSTEM -> Icons.Rounded.Android
                },
                onClick = {
                    onAction(SettingsUiAction.OnThemeModeChanged(mode))
                }
            )
        }
    }
}