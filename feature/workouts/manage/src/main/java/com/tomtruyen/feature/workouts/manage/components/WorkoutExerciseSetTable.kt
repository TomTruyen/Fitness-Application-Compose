package com.tomtruyen.feature.workouts.manage.components

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.ManageWorkoutMode
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.LighterSuccessGreen
import com.tomtruyen.data.models.network.rpc.PreviousExerciseSet
import com.tomtruyen.data.models.ui.WorkoutExerciseSetUiModel
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction

@Composable
fun WorkoutExerciseSetTable(
    workoutExerciseId: String,
    exerciseType: ExerciseType,
    sets: List<WorkoutExerciseSetUiModel>,
    previousSets: List<PreviousExerciseSet>?,
    unit: UnitType,
    mode: ManageWorkoutMode,
    onAction: (ManageWorkoutUiAction) -> Unit,
    onSetClick: (id: String, setIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.animateContentSize(),
    ) {
        WorkoutExerciseSetHeader(
            exerciseType = exerciseType,
            unit = unit,
            mode = mode,
            modifier = Modifier
                .padding(
                    horizontal = Dimens.Normal,
                )
                .padding(bottom = Dimens.Tiny)
        )

        sets.forEachIndexed { setIndex, set ->
            val previousSet = remember(setIndex, previousSets) {
                previousSets?.find { it.sortOrder == setIndex }
            }

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
                workoutExerciseId = workoutExerciseId,
                exerciseType = exerciseType,
                setIndex = setIndex,
                set = set,
                previousSet = previousSet,
                mode = mode,
                onAction = onAction,
                onSetClick = onSetClick
            )
        }
    }
}
