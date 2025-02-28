package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.LighterSuccessGreen
import com.tomtruyen.data.entities.WorkoutExerciseWithSets
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.models.ManageWorkoutMode

@Composable
fun WorkoutExerciseSetTable(
    workoutExercise: WorkoutExerciseWithSets,
    unit: String,
    mode: ManageWorkoutMode,
    onAction: (ManageWorkoutUiAction) -> Unit,
    onSetClick: (id: String, setIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        WorkoutExerciseSetHeader(
            exercise = workoutExercise.exercise.exercise,
            unit = unit,
            mode = mode,
            modifier = Modifier
                .padding(
                    horizontal = Dimens.Normal,
                )
                .padding(bottom = Dimens.Tiny)
        )

        workoutExercise.sets.forEachIndexed { setIndex, set ->
            WorkoutExerciseSetRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = when {
                            set.completed -> LighterSuccessGreen
                            setIndex % 2 == 0 -> MaterialTheme.colorScheme.background
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )
                    .padding(
                        horizontal = Dimens.Normal,
                        vertical = Dimens.Tiny
                    ),
                exercise = workoutExercise,
                setIndex = setIndex,
                set = set,
                mode = mode,
                onAction = onAction,
                onSetClick = onSetClick
            )
        }
    }
}
