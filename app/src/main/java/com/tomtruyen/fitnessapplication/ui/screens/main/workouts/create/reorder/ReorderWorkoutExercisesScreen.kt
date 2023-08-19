package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.navigation.CreateWorkoutNavGraph
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.CreateWorkoutUiEvent
import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.CreateWorkoutViewModel
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.Toolbar
import kotlinx.coroutines.flow.collectLatest
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@CreateWorkoutNavGraph
@Destination
@Composable
fun ReorderWorkoutExercisesScreen(
    navController: NavController,
    parentViewModel: CreateWorkoutViewModel,
    viewModel: ReorderWorkoutExercisesViewModel = koinViewModel {
        parametersOf(parentViewModel.state.value.workout.exercises)
    }
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                is ReorderWorkoutExercisesNavigationType.OnApplyReorder -> {
                    parentViewModel.onEvent(CreateWorkoutUiEvent.OnReorderExercises(navigationType.exercises))
                    navController.popBackStack()
                }
            }
        }
    }

    ReorderWorkoutExercisesScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ReorderWorkoutExercisesScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ReorderWorkoutExercisesUiState,
    onEvent: (ReorderWorkoutExercisesUiEvent) -> Unit
) {
    val reorderState = rememberReorderableLazyListState(onMove = { from, to ->
        onEvent(ReorderWorkoutExercisesUiEvent.OnReorder(from.index, to.index))
    })
    
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.reorder_exercises),
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
                    .reorderable(reorderState)
                    .detectReorderAfterLongPress(reorderState),
            ) {
                items(state.exercises, { it.id }) { workoutExercise ->
                    ReorderableItem(
                        state = reorderState,
                        key = workoutExercise
                    ) { isDragging ->
                        val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp)

                        ReorderListItem(
                            title = workoutExercise.exercise.displayName,
                            modifier = Modifier.shadow(elevation)
                        )
                    }
                }
            }

            Buttons.Default(
                text = stringResource(id = R.string.apply),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Normal)
            ) {
                onEvent(ReorderWorkoutExercisesUiEvent.OnApplyReorder)
            }
        }
    }
}

@Composable
fun ReorderListItem(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(Dimens.Normal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.DragIndicator,
                contentDescription = null,
            )

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Dimens.Small)
            )
        }

        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = 1.dp,
        )
    }
}