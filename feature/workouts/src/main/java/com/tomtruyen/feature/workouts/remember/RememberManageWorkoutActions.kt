package com.tomtruyen.feature.workouts.remember

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.feature.workouts.R
import com.tomtruyen.feature.workouts.WorkoutsUiAction

@Composable
fun rememberWorkoutActions(
    onAction: (WorkoutsUiAction) -> Unit
): List<BottomSheetItem> {
    val errorColor = MaterialTheme.colorScheme.error

    return remember {
        listOf(
            BottomSheetItem(
                titleRes = R.string.action_edit_workout,
                icon = Icons.Rounded.Edit,
                onClick = {
                    onAction(
                        WorkoutsUiAction.Edit
                    )
                }
            ),
            BottomSheetItem(
                titleRes = R.string.action_delete_workout,
                icon = Icons.Rounded.Close,
                onClick = {
                    onAction(
                        WorkoutsUiAction.Delete
                    )
                },
                color = errorColor
            )
        )
    }
}

