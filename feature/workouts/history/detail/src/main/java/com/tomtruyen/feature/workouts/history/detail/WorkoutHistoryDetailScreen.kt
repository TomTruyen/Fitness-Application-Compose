package com.tomtruyen.feature.workouts.history.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.BottomSheetList
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.feature.workouts.history.detail.components.Header
import com.tomtruyen.feature.workouts.history.detail.components.HistoryExerciseItem
import com.tomtruyen.feature.workouts.history.detail.components.MuscleSplitGraph
import com.tomtruyen.feature.workouts.history.detail.components.Statistics
import com.tomtruyen.feature.workouts.history.detail.remember.rememberWorkoutHistoryActions
import com.tomtruyen.navigation.Screen
import com.tomtruyen.navigation.SharedTransitionKey
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.WorkoutHistoryDetailScreen(
    id: String,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: WorkoutHistoryDetailViewModel = koinViewModel {
        parametersOf(id)
    }
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(context, viewModel) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is WorkoutHistoryDetailUiEvent.Navigate.Exercise.Detail -> navController.navigate(
                    Screen.Exercise.Detail(event.id)
                )

                is WorkoutHistoryDetailUiEvent.Navigate.Workout -> navController.navigate(
                    Screen.Workout.Manage(
                        workout = Json.encodeToString(event.workout),
                        mode = event.mode
                    )
                )

                WorkoutHistoryDetailUiEvent.Navigate.Back -> navController.popBackStack()
            }
        }
    }

    WorkoutHistoryDetailScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        animatedVisibilityScope = animatedVisibilityScope,
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )

    BottomSheetList(
        items = rememberWorkoutHistoryActions(
            onAction = viewModel::onAction
        ),
        visible = state.showSheet,
        onDismiss = {
            viewModel.onAction(WorkoutHistoryDetailUiAction.Sheet.Dismiss)
        }
    )

    if(state.showDeleteConfirmation) {
        ConfirmationDialog(
            title = R.string.title_delete_workout,
            message = R.string.message_delete_workout,
            onConfirm = {
                viewModel.onAction(WorkoutHistoryDetailUiAction.Delete)
                viewModel.onAction(WorkoutHistoryDetailUiAction.Dialog.Workout.Dismiss)
            },
            onDismiss = {
                viewModel.onAction(WorkoutHistoryDetailUiAction.Dialog.Workout.Dismiss)
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.WorkoutHistoryDetailScreenLayout(
    snackbarHost: @Composable () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    state: WorkoutHistoryDetailUiState,
    onAction: (WorkoutHistoryDetailUiAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(
                key = SharedTransitionKey.History(state.history.id)
            ),
            animatedVisibilityScope = animatedVisibilityScope
        ),
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(R.string.title_history_detail),
                navController = navController,
                actions = {
                    IconButton(
                        onClick = {
                            onAction(WorkoutHistoryDetailUiAction.Sheet.Show)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.Normal, Alignment.Top),
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Header(
                        workoutName = state.history.name,
                        date = state.history.createdAt
                    )
                }

                item {
                    Statistics(
                        duration = state.history.duration,
                        volume = state.history.volume,
                        sets = state.history.sets,
                        unit = state.history.unit
                    )
                }

                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            vertical = Dimens.Small
                        )
                    )
                }

                item {
                    Label(
                        label = stringResource(id = R.string.label_muscle_split),
                        modifier = Modifier.padding(
                            horizontal = Dimens.Normal
                        )
                    )
                }

                item {
                    MuscleSplitGraph(
                        exercises = state.history.exercises
                    )
                }

                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            vertical = Dimens.Small
                        )
                    )
                }

                item {
                    Label(
                        label = stringResource(id = R.string.label_exercises),
                        modifier = Modifier.padding(
                            horizontal = Dimens.Normal
                        )
                    )
                }

                items(
                    items = state.history.exercises,
                    key = { it.id }
                ) { exercise ->
                    HistoryExerciseItem(
                        exercise = exercise,
                        unit = state.history.unit,
                        onExerciseClick = {
                            onAction(WorkoutHistoryDetailUiAction.Navigate.Exercise.Detail(exercise.exerciseId))
                        },
                        modifier = Modifier.padding(bottom = Dimens.Small)
                    )
                }
            }
        }
    }
}

