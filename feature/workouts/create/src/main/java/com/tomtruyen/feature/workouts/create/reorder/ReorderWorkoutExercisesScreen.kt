package com.tomtruyen.feature.workouts.create.reorder

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.feature.workouts.create.CreateWorkoutUiAction
import com.tomtruyen.feature.workouts.create.CreateWorkoutViewModel
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.feature.workouts.create.R
import com.tomtruyen.feature.workouts.create.reorder.components.ReorderListItem
import kotlinx.coroutines.flow.collectLatest
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.tomtruyen.core.common.R as CommonR

@Composable
fun ReorderWorkoutExercisesScreen(
    navController: NavController,
    parentViewModel: CreateWorkoutViewModel,
    viewModel: ReorderWorkoutExercisesViewModel = koinViewModel {
        parametersOf(parentViewModel.uiState.value.workout.exercises)
    }
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is ReorderWorkoutExercisesUiEvent.OnApplyReorder -> {
                    parentViewModel.onAction(CreateWorkoutUiAction.OnReorderExercises(event.exercises))
                    navController.popBackStack()
                }
            }
        }
    }

    ReorderWorkoutExercisesScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ReorderWorkoutExercisesScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ReorderWorkoutExercisesUiState,
    onAction: (ReorderWorkoutExercisesUiAction) -> Unit
) {
    val reorderState = rememberReorderableLazyListState(onMove = { from, to ->
        onAction(ReorderWorkoutExercisesUiAction.OnReorder(from.index, to.index))
    })
    
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.title_reorder_exercises),
                navController = navController,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LazyColumn(
                state = reorderState.listState,
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
                    .reorderable(reorderState)
                    .detectReorderAfterLongPress(reorderState),
            ) {
                items(state.exercises, { it }) { workoutExercise ->
                    ReorderableItem(
                        state = reorderState,
                        key = workoutExercise
                    ) { isDragging ->
                        val alpha by animateFloatAsState(if (isDragging) 0.25f else 1f, label = "")

                        ReorderListItem(
                            title = workoutExercise.exercise.displayName,
                            modifier = Modifier.alpha(alpha)
                        )
                    }
                }
            }

            Buttons.Default(
                text = stringResource(id = CommonR.string.button_apply),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Normal)
            ) {
                onAction(ReorderWorkoutExercisesUiAction.OnApplyReorder)
            }
        }
    }
}