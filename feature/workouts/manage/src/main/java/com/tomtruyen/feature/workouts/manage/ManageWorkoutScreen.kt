package com.tomtruyen.feature.workouts.manage

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.models.ManageWorkoutMode
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.BottomSheetList
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.core.ui.toolbars.ToolbarTitle
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.feature.workouts.manage.components.ExerciseList
import com.tomtruyen.feature.workouts.manage.components.WorkoutStatistics
import com.tomtruyen.feature.workouts.manage.components.WorkoutTimer
import com.tomtruyen.feature.workouts.manage.remember.rememberExerciseActions
import com.tomtruyen.feature.workouts.manage.remember.rememberSetActions
import com.tomtruyen.feature.workouts.manage.remember.rememberWorkoutActions
import com.tomtruyen.navigation.NavArguments
import com.tomtruyen.navigation.Screen
import com.tomtruyen.navigation.SharedTransitionKey
import kotlinx.coroutines.flow.collectLatest
import java.util.concurrent.TimeUnit
import com.tomtruyen.core.common.R as CommonR

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ManageWorkoutScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: ManageWorkoutViewModel
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ManageWorkoutUiEvent.Navigate.Back -> navController.popBackStack()

                is ManageWorkoutUiEvent.Navigate.Exercise.Add -> navController.navigate(
                    Screen.Exercise.Overview(
                        Screen.Exercise.Overview.Mode.SELECT
                    )
                )

                is ManageWorkoutUiEvent.Navigate.Exercise.Replace -> navController.navigate(
                    Screen.Exercise.Overview(
                        Screen.Exercise.Overview.Mode.REPLACE
                    )
                )

                is ManageWorkoutUiEvent.Navigate.Exercise.Detail -> navController.navigate(
                    Screen.Exercise.Detail(event.id)
                )

                is ManageWorkoutUiEvent.Navigate.History.Detail -> navController.navigate(
                    Screen.History.Detail(event.workoutHistoryId)
                ) {
                    popUpTo<Screen.Workout.Overview> {
                        inclusive = false
                    }
                }

                is ManageWorkoutUiEvent.Navigate.Workout.Edit -> navController.navigate(
                    Screen.Workout.Manage(event.id, ManageWorkoutMode.EDIT),
                )

                is ManageWorkoutUiEvent.Navigate.Workout.Execute -> navController.navigate(
                    Screen.Workout.Manage(event.id, ManageWorkoutMode.EXECUTE)
                )

                is ManageWorkoutUiEvent.ScrollToExercise -> {
                    lazyListState.animateScrollToItem(
                        event.index.coerceIn(
                            0,
                            state.workout.exercises.size - 1
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(navController) {
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<Pair<Screen.Exercise.Overview.Mode, List<ExerciseUiModel>>?>(
            NavArguments.EXERCISES,
            null
        )
            ?.collectLatest { result ->
                if (result != null) {
                    val (mode, exercises) = result

                    when (mode) {
                        Screen.Exercise.Overview.Mode.REPLACE -> {
                            exercises.firstOrNull()?.let { exercise ->
                                viewModel.onAction(ManageWorkoutUiAction.Exercise.Replace(exercise))
                            }
                        }

                        Screen.Exercise.Overview.Mode.SELECT -> {
                            viewModel.onAction(ManageWorkoutUiAction.Exercise.Add(exercises))
                        }

                        else -> Unit
                    }
                }

                navController.currentBackStackEntry?.savedStateHandle?.remove<Pair<Screen.Exercise.Overview.Mode, List<ExerciseUiModel>>?>(
                    NavArguments.EXERCISES
                )
            }
    }

    ManageWorkoutScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        animatedVisibilityScope = animatedVisibilityScope,
        navController = navController,
        state = state,
        lazyListState = lazyListState,
        onAction = viewModel::onAction,
    )

    BottomSheetList(
        items = rememberWorkoutActions(
            onAction = viewModel::onAction
        ),
        visible = state.showWorkoutMoreActions,
        onDismiss = { viewModel.onAction(ManageWorkoutUiAction.Sheet.Workout.Dismiss) },
    )

    BottomSheetList(
        items = rememberExerciseActions(
            onAction = viewModel::onAction
        ),
        visible = state.showExerciseMoreActions,
        onDismiss = { viewModel.onAction(ManageWorkoutUiAction.Sheet.Exercise.Dismiss) },
    )

    BottomSheetList(
        items = rememberSetActions(
            selectedExerciseId = state.selectedExerciseId,
            selectedSetIndex = state.selectedSetIndex,
            onAction = viewModel::onAction
        ),
        visible = state.showSetMoreActions,
        onDismiss = { viewModel.onAction(ManageWorkoutUiAction.Sheet.Set.Dismiss) },
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ManageWorkoutScreenLayout(
    snackbarHost: @Composable () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    state: ManageWorkoutUiState,
    lazyListState: LazyListState,
    onAction: (ManageWorkoutUiAction) -> Unit,
) {
    val workoutDuration = remember(state.workout.duration) {
        TimeUtils.formatSeconds(
            seconds = state.workout.duration,
            alwaysShow = listOf(TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS)
        )
    }

    var confirmationDialogVisible by remember { mutableStateOf(false) }

    val onNavigateUp: () -> Unit = {
        if (!state.mode.isView && state.workout.exercises != state.initialWorkout.exercises) {
            confirmationDialogVisible = true
        } else {
            onAction(ManageWorkoutUiAction.Workout.Discard)
        }
    }

    BackHandler(enabled = !confirmationDialogVisible, onBack = onNavigateUp)

    Scaffold(
        snackbarHost = snackbarHost,
        modifier = Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(
                key = SharedTransitionKey.Workout(state.workout.id)
            ),
            animatedVisibilityScope = animatedVisibilityScope
        ),
        topBar = {
            Toolbar(
                title = {
                    if (state.mode.isView) {
                        ToolbarTitle(title = state.workout.name)
                    } else {
                        TextFields.Default(
                            modifier = Modifier.padding(
                                end = Dimens.Tiny,
                            ),
                            textFieldModifier = Modifier.defaultMinSize(minHeight = 36.dp),
                            padding = PaddingValues(Dimens.Small),
                            placeholder = stringResource(id = R.string.title_workout_name),
                            value = state.workout.name,
                            onValueChange = { name ->
                                onAction(
                                    ManageWorkoutUiAction.Workout.OnNameChanged(
                                        name = name
                                    )
                                )
                            }
                        )
                    }
                },
                navController = navController,
                onNavigateUp = onNavigateUp
            ) {
                if (state.mode.isExecute) {
                    WorkoutTimer(
                        time = workoutDuration,
                        modifier = Modifier
                            .height(36.dp)
                            .padding(end = Dimens.Small)
                    )
                }

                if (state.mode.isView) {
                    IconButton(
                        onClick = {
                            onAction(ManageWorkoutUiAction.Sheet.Workout.Show)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }
                } else {
                    Buttons.Default(
                        enabled = state.workout.exercises.isNotEmpty(),
                        modifier = Modifier.padding(end = Dimens.Small),
                        text = stringResource(id = CommonR.string.button_save),
                        contentPadding = PaddingValues(0.dp),
                        minButtonSize = 36.dp,
                        onClick = {
                            onAction(ManageWorkoutUiAction.Workout.Save)
                        }
                    )
                }
            }
        }
    ) {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {

                if (state.mode.isExecute) {
                    WorkoutStatistics(
                        modifier = Modifier.fillMaxWidth(),
                        volume = state.workout.totalVolumeCompleted,
                        reps = state.workout.repsCountCompleted,
                        sets = state.workout.setsCountCompleted,
                        unit = state.workout.unit
                    )
                }

                ExerciseList(
                    modifier = Modifier.weight(1f),
                    animatedVisibilityScope = animatedVisibilityScope,
                    state = state,
                    lazyListState = lazyListState,
                    onAction = onAction,
                    listHeader = {
                        // Items that will be prepended to the top of the list
                        if (state.mode.isView) {
                            item {
                                Buttons.Default(
                                    text = stringResource(id = R.string.title_start_workout),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(Dimens.Normal)
                                ) {
                                    onAction(ManageWorkoutUiAction.Navigate.Workout.Execute)
                                }
                            }

                            item {
                                Label(
                                    modifier = Modifier.padding(start = Dimens.Normal),
                                    label = stringResource(id = R.string.label_exercises)
                                )
                            }
                        }
                    }
                )
            }



            if (confirmationDialogVisible) {
                ConfirmationDialog(
                    title = CommonR.string.title_unsaved_changes,
                    message = CommonR.string.message_unsaved_changes,
                    onConfirm = {
                        onAction(
                            ManageWorkoutUiAction.Workout.Discard
                        )
                        confirmationDialogVisible = false
                    },
                    onDismiss = {
                        confirmationDialogVisible = false
                    },
                    confirmText = CommonR.string.button_discard
                )
            }
        }
    }
}