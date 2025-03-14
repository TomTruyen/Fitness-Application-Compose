package com.tomtruyen.feature.profile.remember

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tomtruyen.core.common.ThemeMode
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.feature.profile.ProfileUiAction

@Composable
fun rememberThemeModeActions(
    onAction: (ProfileUiAction) -> Unit
): List<BottomSheetItem> {
    return remember {
        ThemeMode.entries.map { mode ->
            BottomSheetItem(
                title = mode.value,
                icon = when(mode) {
                    ThemeMode.DARK -> Icons.Default.DarkMode
                    ThemeMode.LIGHT -> Icons.Default.LightMode
                    ThemeMode.SYSTEM -> Icons.Default.Android
                },
                onClick = {
                    onAction(ProfileUiAction.OnThemeModeChanged(mode))
                }
            )
        }
    }
}