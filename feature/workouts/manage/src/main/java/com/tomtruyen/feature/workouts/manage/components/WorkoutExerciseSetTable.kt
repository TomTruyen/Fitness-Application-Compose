package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.common.models.WorkoutMode
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.success
import com.tomtruyen.core.ui.swipereveal.SwipeToRevealAction
import com.tomtruyen.core.ui.swipereveal.SwipeToRevealBox
import com.tomtruyen.core.ui.swipereveal.rememberSwipeToRevealState
import com.tomtruyen.data.entities.PreviousSet
import com.tomtruyen.data.models.ui.WorkoutExerciseSetUiModel
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.core.common.R as CommonR

@Composable
fun WorkoutExerciseSetTable(
    workoutExerciseId: String,
    exerciseType: ExerciseType,
    sets: List<WorkoutExerciseSetUiModel>,
    previousSets: List<PreviousSet>?,
    unit: UnitType,
    mode: WorkoutMode,
    onAction: (ManageWorkoutUiAction) -> Unit,
    onSetClick: (id: String, setIndex: Int) -> Unit,
    onPreviousSetClick: (id: String, setIndex: Int, previousSet: PreviousSet) -> Unit,
    modifier: Modifier = Modifier
) {
    val successBackgroundColor = MaterialTheme.colorScheme.success
    val evenIndexBackgroundColor = MaterialTheme.colorScheme.background
    val oddIndexBackgroundColor = MaterialTheme.colorScheme.surface

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

            val backgroundColor = remember(set.completed, setIndex, successBackgroundColor.value) {
                when {
                    set.completed -> successBackgroundColor.value
                    setIndex % 2 == 0 -> evenIndexBackgroundColor
                    else -> oddIndexBackgroundColor
                }
            }

            val swipeToRevealState = rememberSwipeToRevealState(set.id)

            SwipeToRevealBox(
                state = swipeToRevealState,
                enabled = !mode.isView,
                actions = {
                    SwipeToRevealAction(
                        icon = Icons.Rounded.Delete,
                        text = stringResource(id = CommonR.string.button_delete),
                        backgroundColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        outerBackgroundColor = backgroundColor,
                        onClick = {
                            onAction(
                                ManageWorkoutUiAction.Set.Delete(
                                    exerciseId = workoutExerciseId,
                                    setIndex = setIndex
                                )
                            )
                        }
                    )
                }
            ) {
                WorkoutExerciseSetRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = backgroundColor
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
                    onSetClick = onSetClick,
                    onPreviousSetClick = onPreviousSetClick
                )
            }
        }
    }
}
