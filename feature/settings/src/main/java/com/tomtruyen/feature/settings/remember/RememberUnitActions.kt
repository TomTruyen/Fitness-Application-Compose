package com.tomtruyen.feature.settings.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.feature.settings.SettingsUiAction

@Composable
fun rememberUnitActions(
    onAction: (SettingsUiAction) -> Unit
): List<BottomSheetItem> {
    return remember {
        UnitType.entries.map { unit ->
            BottomSheetItem(
                title = unit.value,
                onClick = {
                    onAction(SettingsUiAction.OnUnitChanged(unit))
                }
            )
        }
    }
}