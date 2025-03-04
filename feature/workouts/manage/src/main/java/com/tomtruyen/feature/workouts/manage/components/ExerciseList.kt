package com.tomtruyen.feature.workouts.manage.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiState
import com.tomtruyen.feature.workouts.manage.R
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun ExerciseList(
    modifier: Modifier = Modifier,
    state: ManageWorkoutUiState,
    lazyListState: LazyListState,
    listHeader: LazyListScope.() -> Unit = {},
    onAction: (ManageWorkoutUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onAction(ManageWorkoutUiAction.OnReorder(from.index, to.index))
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize()
    ) {
        listHeader()
        
        items(
            state.workout.exercises,
            key = { it.id }) { exercise ->
            ReorderableItem(
                state = reorderableLazyListState,
                key = exercise.id,
                enabled = !state.mode.isView
            ) { isDragging ->
                val alpha by animateFloatAsState(if (isDragging) 0.25f else 1f, label = "")

                ExerciseListItem(
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
                    unit = state.settings.unit,
                    mode = state.mode,
                    onAction = onAction,
                )
            }

        }

        if(!state.mode.isView) {
            item {
                Buttons.Default(
                    text = stringResource(id = R.string.button_add_exercise),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = Dimens.Normal,
                            vertical = Dimens.Small
                        ),
                ) {
                    onAction(ManageWorkoutUiAction.OnAddExerciseClicked)
                }
            }
        }
    }
}