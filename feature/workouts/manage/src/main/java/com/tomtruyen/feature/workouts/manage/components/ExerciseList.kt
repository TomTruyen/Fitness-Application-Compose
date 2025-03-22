package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.workout.exercise.ExerciseItem
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiState
import com.tomtruyen.feature.workouts.manage.R
import com.tomtruyen.feature.workouts.manage.WorkoutExerciseActions
import com.tomtruyen.feature.workouts.manage.WorkoutSetActions
import com.tomtruyen.navigation.SharedTransitionKey
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import com.tomtruyen.core.common.R as CommonR

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ExerciseList(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    state: ManageWorkoutUiState,
    lazyListState: LazyListState,
    listHeader: LazyListScope.() -> Unit = {},
    onDiscard: () -> Unit,
    onAction: (ManageWorkoutUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onAction(ManageWorkoutUiAction.Exercise.Reorder(from.index, to.index))
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize()
    ) {
        listHeader()

        items(
            items = state.workout.exercises,
            key = { it.id }
        ) { exercise ->
            ReorderableItem(
                state = reorderableLazyListState,
                key = exercise.id,
                enabled = !state.mode.isView
            ) { isDragging ->
                val alpha by animateFloatAsState(if (isDragging) 0.25f else 1f, label = "")

                val previousSets = remember(exercise.exerciseId, state.previousSets) {
                    state.previousSets[exercise.exerciseId]
                }

                ExerciseItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .longPressDraggableHandle(
                            enabled = !state.mode.isView,
                            onDragStarted = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                        )
                        .alpha(alpha),
                    exercise = exercise,
                    previousSets = previousSets,
                    unit = state.settings.unit,
                    mode = state.mode,
                    onAction = remember {
                        WorkoutExerciseActions(onAction)
                    },
                    onSetAction = remember {
                        WorkoutSetActions(onAction)
                    }
                )
            }

        }

        if (!state.mode.isView) {
            item {
                Buttons.Default(
                    text = stringResource(id = R.string.button_add_exercise),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = Dimens.Normal,
                            vertical = Dimens.Small
                        )
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = SharedTransitionKey.Exercise.KEY_WORKOUT_ADD_EXERCISE
                            ),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                ) {
                    onAction(ManageWorkoutUiAction.Exercise.OnAddClicked)
                }
            }
        }

        if (state.mode.isExecute) {
            item {
                Buttons.Text(
                    text = stringResource(id = CommonR.string.button_discard),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = onDiscard
                )
            }
        }
    }
}