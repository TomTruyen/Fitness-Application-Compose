package com.tomtruyen.feature.workouts.remember

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.feature.workouts.history.R
import com.tomtruyen.feature.workouts.history.WorkoutHistoryUiAction

@Composable
fun rememberWorkoutHistoryActions(
    onAction: (WorkoutHistoryUiAction) -> Unit
): List<BottomSheetItem> {
    val errorColor = MaterialTheme.colorScheme.error

    return remember {
        listOf(
            BottomSheetItem(
                titleRes = R.string.action_start_workout,
                icon = Icons.Rounded.PlayArrow,
                onClick = {
                    onAction(WorkoutHistoryUiAction.Workout.Start)
                },
            ),
            BottomSheetItem(
                titleRes = R.string.action_save_as_workout,
                icon = Icons.Rounded.ContentCopy,
                onClick = {
                    onAction(WorkoutHistoryUiAction.Workout.Save)
                }
            ),
            BottomSheetItem(
                titleRes = R.string.action_delete_workout,
                icon = Icons.Rounded.Close,
                onClick = {
                    onAction(
                        WorkoutHistoryUiAction.Dialog.Workout.Show
                    )
                },
                color = errorColor
            )
        )
    }
}