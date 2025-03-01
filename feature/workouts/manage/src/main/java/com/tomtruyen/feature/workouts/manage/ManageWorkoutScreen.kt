package com.tomtruyen.feature.workouts.manage

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.core.ui.BottomSheetList
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.core.ui.toolbars.ToolbarTitle
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.feature.workouts.manage.components.ExerciseList
import com.tomtruyen.feature.workouts.manage.components.WorkoutExerciseHeader
import com.tomtruyen.feature.workouts.manage.components.WorkoutExerciseSetTable
import com.tomtruyen.feature.workouts.manage.components.WorkoutStatistics
import com.tomtruyen.feature.workouts.manage.components.WorkoutTimer
import com.tomtruyen.feature.workouts.manage.models.ManageWorkoutMode
import com.tomtruyen.navigation.NavArguments
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.util.concurrent.TimeUnit
import com.tomtruyen.core.common.R as CommonR

@Composable
fun ManageWorkoutScreen(
    navController: NavController,
    viewModel: ManageWorkoutViewModel
) {
    val context = LocalContext.current
    val errorColor = MaterialTheme.colorScheme.error

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    val exerciseActions = remember {
        listOf(
            BottomSheetItem(
                title = R.string.action_exercise_replace,
                icon = Icons.Default.Sync,
                onClick = {
                    viewModel.onAction(ManageWorkoutUiAction.OnReplaceExerciseClicked)
                }
            ),
            BottomSheetItem(
                title = R.string.action_remove_exercise,
                icon = Icons.Default.Close,
                onClick = {
                    viewModel.onAction(ManageWorkoutUiAction.OnDeleteExercise)
                },
                color = errorColor
            ),
        )
    }

    val setActions = remember {
        listOf(
            BottomSheetItem(
                title = R.string.action_remove_set,
                icon = Icons.Default.Close,
                onClick = {
                    if (state.selectedExerciseId != null && state.selectedSetIndex != null) {
                        viewModel.onAction(
                            ManageWorkoutUiAction.OnDeleteSet(
                                state.selectedExerciseId!!,
                                state.selectedSetIndex!!
                            )
                        )
                    }
                },
                color = errorColor
            ),
        )
    }

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ManageWorkoutUiEvent.NavigateBack -> navController.popBackStack()
                is ManageWorkoutUiEvent.NavigateToReplaceExercise -> navController.navigate(
                    Screen.Exercise.Overview(
                        Screen.Exercise.Overview.Mode.REPLACE
                    )
                )

                is ManageWorkoutUiEvent.NavigateToAddExercise -> navController.navigate(
                    Screen.Exercise.Overview(
                        Screen.Exercise.Overview.Mode.SELECT
                    )
                )

                is ManageWorkoutUiEvent.NavigateToDetail -> navController.navigate(
                    Screen.Workout.Detail(
                        event.id
                    )
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
                                viewModel.onAction(ManageWorkoutUiAction.OnReplaceExercise(exercise))
                            }
                        }

                        Screen.Exercise.Overview.Mode.SELECT -> {
                            viewModel.onAction(ManageWorkoutUiAction.OnAddExercises(exercises))
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
        navController = navController,
        state = state,
        lazyListState = lazyListState,
        onAction = viewModel::onAction,
    )

    BottomSheetList(
        items = exerciseActions,
        visible = state.showExerciseMoreActions,
        onDismiss = { viewModel.onAction(ManageWorkoutUiAction.ToggleExerciseMoreActionSheet()) },
    )

    BottomSheetList(
        items = setActions,
        visible = state.showSetMoreActions,
        onDismiss = { viewModel.onAction(ManageWorkoutUiAction.ToggleSetMoreActionSheet()) },
    )
}

@Composable
fun ManageWorkoutScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ManageWorkoutUiState,
    lazyListState: LazyListState,
    onAction: (ManageWorkoutUiAction) -> Unit,
) {
    val workoutDuration = remember(state.duration) {
        TimeUtils.formatSeconds(
            seconds = state.duration,
            alwaysShow = listOf(TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS)
        )
    }

    var confirmationDialogVisible by remember { mutableStateOf(false) }

    val onNavigateUp: () -> Unit = {
        if (state.workout.exercises != state.initialWorkout.exercises) {
            confirmationDialogVisible = true
        } else {
            navController.popBackStack()
        }
    }

    BackHandler(enabled = !confirmationDialogVisible, onBack = onNavigateUp)

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = {
                    if (state.mode == ManageWorkoutMode.EXECUTE) {
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
                                    ManageWorkoutUiAction.OnWorkoutNameChanged(
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
                AnimatedVisibility(
                    visible = state.mode == ManageWorkoutMode.EXECUTE
                ) {
                    WorkoutTimer(
                        time = workoutDuration,
                        modifier = Modifier
                            .height(36.dp)
                            .padding(end = Dimens.Small)
                    )
                }

                Buttons.Default(
                    enabled = state.workout.exercises.isNotEmpty(),
                    modifier = Modifier.padding(end = Dimens.Small),
                    text = stringResource(id = CommonR.string.button_save),
                    contentPadding = PaddingValues(0.dp),
                    minButtonSize = 36.dp,
                    onClick = {
                        onAction(ManageWorkoutUiAction.Save)
                    }
                )
            }
        }
    ) {
        LoadingContainer(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            loading = state.loading,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                AnimatedVisibility(
                    visible = state.mode == ManageWorkoutMode.EXECUTE,
                ) {
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
                    state = state,
                    lazyListState = lazyListState,
                    onAction = onAction,
                )
            }



            if (confirmationDialogVisible) {
                ConfirmationDialog(
                    title = CommonR.string.title_unsaved_changes,
                    message = CommonR.string.message_unsaved_changes,
                    onConfirm = {
                        navController.popBackStack()
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