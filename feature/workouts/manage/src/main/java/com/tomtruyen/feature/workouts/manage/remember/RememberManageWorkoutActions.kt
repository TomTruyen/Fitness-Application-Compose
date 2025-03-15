package com.tomtruyen.feature.workouts.manage.remember

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.R

@Composable
fun rememberExerciseActions(
    onAction: (ManageWorkoutUiAction) -> Unit
): List<BottomSheetItem> {
    val errorColor = MaterialTheme.colorScheme.error

    return remember {
        listOf(
            BottomSheetItem(
                titleRes = R.string.action_exercise_reorder,
                icon = Icons.Rounded.SwapVert,
                onClick = {
                    onAction(ManageWorkoutUiAction.Exercise.OnReorderClicked)
                }
            ),
            BottomSheetItem(
                titleRes = R.string.action_exercise_replace,
                icon = Icons.Rounded.Sync,
                onClick = {
                    onAction(ManageWorkoutUiAction.Exercise.OnReplaceClicked)
                }
            ),
            BottomSheetItem(
                titleRes = R.string.action_remove_exercise,
                icon = Icons.Rounded.Close,
                onClick = {
                    onAction(ManageWorkoutUiAction.Exercise.Delete)
                },
                color = errorColor
            ),
        )
    }
}

@Composable
fun rememberSetActions(
    selectedExerciseId: String?,
    selectedSetIndex: Int?,
    onAction: (ManageWorkoutUiAction) -> Unit
): List<BottomSheetItem> {
    val errorColor = MaterialTheme.colorScheme.error

    return remember(selectedSetIndex, selectedExerciseId) {
        listOf(
            BottomSheetItem(
                titleRes = R.string.action_remove_set,
                icon = Icons.Rounded.Close,
                onClick = {
                    if (selectedExerciseId != null && selectedSetIndex != null) {
                        onAction(
                            ManageWorkoutUiAction.Set.Delete(
                                selectedExerciseId,
                                selectedSetIndex
                            )
                        )
                    }
                },
                color = errorColor
            ),
        )
    }
}

@Composable
fun rememberWorkoutActions(
    onAction: (ManageWorkoutUiAction) -> Unit
): List<BottomSheetItem> {
    val errorColor = MaterialTheme.colorScheme.error

    return remember {
        listOf(
            BottomSheetItem(
                titleRes = R.string.action_edit_workout,
                icon = Icons.Rounded.Edit,
                onClick = {
                    onAction(
                        ManageWorkoutUiAction.Navigate.Workout.Edit
                    )
                }
            ),
            BottomSheetItem(
                titleRes = R.string.action_delete_workout,
                icon = Icons.Rounded.Close,
                onClick = {
                    onAction(
                        ManageWorkoutUiAction.Dialog.Workout.Show
                    )
                },
                color = errorColor
            )
        )
    }
}