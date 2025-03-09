package com.tomtruyen.feature.profile.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.feature.profile.ProfileUiAction

@Composable
fun rememberUnitActions(
    onAction: (ProfileUiAction) -> Unit
): List<BottomSheetItem> {
    return remember {
        UnitType.entries.map { unit ->
            BottomSheetItem(
                title = unit.value,
                onClick = {
                    onAction(ProfileUiAction.OnUnitChanged(unit))
                }
            )
        }
    }
}