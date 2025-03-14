package com.tomtruyen.feature.workouts.history.detail.remember

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.feature.workouts.history.detail.R
import com.tomtruyen.feature.workouts.history.detail.WorkoutHistoryDetailUiAction

@Composable
fun rememberWorkoutHistoryActions(
    onAction: (WorkoutHistoryDetailUiAction) -> Unit
): List<BottomSheetItem> {
    return remember {
        listOf(
            BottomSheetItem(
                titleRes = R.string.action_save_as_workout,
                icon = Icons.Rounded.ContentCopy,
                onClick = {
                    onAction(WorkoutHistoryDetailUiAction.Workout.Save)
                }
            ),
            BottomSheetItem(
                titleRes = R.string.action_start_workout,
                icon = Icons.Rounded.PlayArrow,
                onClick = {
                    onAction(WorkoutHistoryDetailUiAction.Workout.Start)
                },
            ),
        )
    }
}