package com.tomtruyen.core.ui.workout.exercise

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
import com.tomtruyen.core.common.models.BaseExercise
import com.tomtruyen.core.common.models.BaseSet
import com.tomtruyen.core.common.models.actions.SetActions
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.common.models.WorkoutMode
import com.tomtruyen.core.common.models.actions.ExerciseActions
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.R
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.workout.set.SetTable

@Composable
fun ExerciseItem(
    exercise: BaseExercise,
    previousSets: List<BaseSet>?,
    unit: UnitType,
    mode: WorkoutMode,
    onAction: ExerciseActions,
    onSetAction: SetActions,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        // Header
        ExerciseHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Normal),
            name = exercise.displayName,
            imageUrl = exercise.imageUrl,
            mode = mode,
            onTitleClick = {
                onAction.navigateDetail(exercise.id)
            },
            onActionClick = {
                onAction.showSheet(exercise.id)
            },
        )

        // Notes
        TextFields.Default(
            modifier = Modifier.padding(
                start = Dimens.Normal,
                end = Dimens.Normal,
                bottom = Dimens.Small
            ),
            readOnly = mode.isView,
            singleLine = false,
            padding = PaddingValues(Dimens.Small),
            placeholder = stringResource(id = R.string.placeholder_notes),
            value = exercise.notes,
            onValueChange = { notes ->
                onAction.notesChanged(
                    id = exercise.id,
                    notes = notes
                )
            }
        )

        // Sets
        SetTable(
            workoutExerciseId = exercise.id,
            exerciseType = exercise.type,
            sets = exercise.sets,
            previousSets = previousSets,
            unit = unit,
            mode = mode,
            onSetClick = { id, setIndex ->
                onSetAction.showSheet(
                    exerciseId = id,
                    setIndex = setIndex
                )
            },
            onAction = onSetAction
        )

        if (!mode.isView) {
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
                    onSetAction.add(exercise.id)
                }
            )
        }
    }
}