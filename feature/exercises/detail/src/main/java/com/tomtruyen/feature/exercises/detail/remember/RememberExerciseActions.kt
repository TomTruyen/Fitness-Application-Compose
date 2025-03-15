package com.tomtruyen.feature.exercises.detail.remember

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.feature.exercises.detail.ExerciseDetailUiAction
import com.tomtruyen.feature.exercises.detail.R

@Composable
fun rememberExerciseActions(
    onAction: (ExerciseDetailUiAction) -> Unit
): List<BottomSheetItem> {
    val errorColor = MaterialTheme.colorScheme.error

    return remember {
        listOf(
            BottomSheetItem(
                titleRes = R.string.action_edit_exercise,
                icon = Icons.Rounded.Edit,
                onClick = {
                    onAction(ExerciseDetailUiAction.Edit)
                }
            ),
            BottomSheetItem(
                titleRes = R.string.action_delete_exercise,
                icon = Icons.Rounded.Close,
                onClick = {
                    onAction(ExerciseDetailUiAction.Dialog.Show)
                },
                color = errorColor
            ),
        )
    }
}