package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.R
import com.tomtruyen.feature.workouts.manage.models.ManageWorkoutMode

@Composable
fun ExerciseListItem(
    exercise: WorkoutExerciseUiModel,
    unit: UnitType,
    mode: ManageWorkoutMode,
    onAction: (ManageWorkoutUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        // Header
        WorkoutExerciseHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Normal),
            name = exercise.displayName,
            imageUrl = exercise.imageUrl,
            onTitleClick = {
                onAction(
                    ManageWorkoutUiAction.NavigateExerciseDetail(
                        id = exercise.id
                    )
                )
            },
            onActionClick = {
                onAction(
                    ManageWorkoutUiAction.ShowExerciseMoreActionSheet(
                        id = exercise.id
                    )
                )
            },
        )

        // Notes
        TextFields.Default(
            modifier = Modifier.padding(
                start = Dimens.Normal,
                end = Dimens.Normal,
                bottom = Dimens.Small
            ),
            singleLine = false,
            border = true,
            padding = PaddingValues(Dimens.Small),
            placeholder = stringResource(id = R.string.placeholder_notes),
            value = exercise.notes,
            onValueChange = { notes ->
                onAction(
                    ManageWorkoutUiAction.OnExerciseNotesChanged(
                        id = exercise.id,
                        notes = notes
                    )
                )
            }
        )

        // Sets
        WorkoutExerciseSetTable(
            workoutExerciseId = exercise.id,
            exerciseType = exercise.type,
            sets = exercise.sets,
            unit = unit,
            mode = mode,
            onSetClick = { id, setIndex ->
                onAction(
                    ManageWorkoutUiAction.ShowSetMoreActionSheet(
                        id = id,
                        setIndex = setIndex
                    )
                )
            },
            onAction = onAction
        )

        // Add Set Button
        Buttons.Default(
            text = stringResource(id = R.string.button_add_set),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Normal),
            minButtonSize = 0.dp,
            contentPadding = PaddingValues(Dimens.Small),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            onClick = {
                onAction(ManageWorkoutUiAction.OnAddSet(exercise.id))
            }
        )
    }
}