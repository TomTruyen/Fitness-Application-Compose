package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse
import com.tomtruyen.feature.workouts.manage.WorkoutExerciseUiAction

@Composable
fun WorkoutExerciseSetTable(
    workoutExercise: WorkoutExerciseResponse,
    unit: String,
    onEvent: (WorkoutExerciseUiAction) -> Unit,
    onSetClick: (id: String, setIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        WorkoutExerciseSetHeader(
            exercise = workoutExercise.exercise,
            unit = unit,
            modifier = Modifier.padding(
                horizontal = Dimens.Normal,
            )
        )

        workoutExercise.sets.forEachIndexed { setIndex, set ->
            WorkoutExerciseSet(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        color = if(setIndex % 2 == 0) {
                            MaterialTheme.colorScheme.background
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .padding(
                        horizontal = Dimens.Normal,
                        vertical = Dimens.Tiny
                    ),
                workoutExercise = workoutExercise,
                setIndex = setIndex,
                set = set,
                onEvent = onEvent,
                onSetClick = onSetClick
            )
        }
    }
}
