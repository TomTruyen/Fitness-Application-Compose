package com.tomtruyen.feature.workouts.manage

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.BottomSheetItem
import com.tomtruyen.core.ui.BottomSheetList
import com.tomtruyen.navigation.NavArguments
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.toolbars.ToolbarTitle
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse
import com.tomtruyen.feature.workouts.shared.WorkoutExerciseEvent
import com.tomtruyen.feature.workouts.shared.ui.WorkoutExerciseHeader
import com.tomtruyen.feature.workouts.shared.ui.WorkoutExerciseSetTable
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import com.tomtruyen.core.common.R as CommonR

@Composable
fun ManageWorkoutScreen(
    navController: NavController,
    viewModel: ManageWorkoutViewModel
) {
    val context = LocalContext.current
    val errorColor = MaterialTheme.colorScheme.error


    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val actions = remember {
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

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is ManageWorkoutUiEvent.NavigateBack -> navController.popBackStack()
                is ManageWorkoutUiEvent.NavigateToReplaceExercise -> navController.navigate(Screen.Exercise.Overview(Screen.Exercise.Overview.Mode.REPLACE))
                is ManageWorkoutUiEvent.NavigateToAddExercise -> navController.navigate(Screen.Exercise.Overview(Screen.Exercise.Overview.Mode.SELECT))
            }
        }
    }

    LaunchedEffect(navController) {
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<Pair<Screen.Exercise.Overview.Mode, List<Exercise>>?>(NavArguments.EXERCISES, null)
            ?.collectLatest { result ->
                if(result != null) {
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

                navController.currentBackStackEntry?.savedStateHandle?.remove<Pair<Screen.Exercise.Overview.Mode, List<Exercise>>?>(NavArguments.EXERCISES)
            }
    }

    ManageWorkoutScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction,
        onWorkoutEvent = viewModel::onWorkoutEvent
    )

    BottomSheetList(
        items = actions,
        visible = state.isMoreActionsSheetVisible,
        onDismiss = { viewModel.onAction(ManageWorkoutUiAction.DismissMoreActionsSheet) },
    )
}

@Composable
fun ManageWorkoutScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ManageWorkoutUiState,
    onAction: (ManageWorkoutUiAction) -> Unit,
    onWorkoutEvent: (WorkoutExerciseEvent) -> Unit
) {
    var confirmationDialogVisible by remember { mutableStateOf(false) }

    val onNavigateUp: () -> Unit = {
        if(state.workout.exercises != state.initialWorkout.exercises) {
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
                    // TODO: Add Logic to show title only when not editing
                    if(true) {
                        TextFields.Default(
                            modifier = Modifier.padding(
                                end = if(state.workout.exercises.isEmpty()) {
                                    Dimens.Small
                                } else {
                                    Dimens.Normal
                                }
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
                    } else {
                        ToolbarTitle(title = stringResource(id = R.string.title_create_workout))
                    }
                },
                navController = navController,
                onNavigateUp = onNavigateUp
            ) {
                AnimatedVisibility(
                    visible = state.workout.exercises.isNotEmpty(),
                ) {
                    Buttons.Default(
                        text = stringResource(id = CommonR.string.button_save),
                        contentPadding = PaddingValues(0.dp),
                        minButtonSize = 36.dp,
                        onClick = {
                            onAction(ManageWorkoutUiAction.Save)
                        }
                    )
                }
            }
        }
    ) {
        LoadingContainer(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            loading = state.loading,
        ) {
            ExerciseList(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onAction = onAction,
                onWorkoutEvent = onWorkoutEvent
            )

            if(confirmationDialogVisible) {
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

@Composable
fun ExerciseList(
    modifier: Modifier = Modifier,
    state: ManageWorkoutUiState,
    onAction: (ManageWorkoutUiAction) -> Unit,
    onWorkoutEvent: (WorkoutExerciseEvent) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onAction(ManageWorkoutUiAction.OnReorder(from.index, to.index))
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize()
    ) {
        items(state.workout.exercises, key = { it }) { exercise ->
            ReorderableItem(
                state = reorderableLazyListState,
                key = exercise
            ) { isDragging ->
                val alpha by animateFloatAsState(if (isDragging) 0.25f else 1f, label = "")

                ExerciseListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .longPressDraggableHandle(
                            onDragStarted = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                        )
                        .alpha(alpha),
                    workoutExercise = exercise,
                    unit = state.settings.unit,
                    onAction = onAction,
                    onWorkoutEvent = onWorkoutEvent
                )
            }

        }

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

@Composable
fun ExerciseListItem(
    workoutExercise: WorkoutExerciseResponse,
    unit: String,
    onAction: (ManageWorkoutUiAction) -> Unit,
    onWorkoutEvent: (WorkoutExerciseEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        // Header
        WorkoutExerciseHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Normal),
            exercise = workoutExercise.exercise,
            onActionClick = {
                onAction(
                    ManageWorkoutUiAction.ShowMoreActionSheet(
                        id = workoutExercise.id
                    )
                )
            },
        )

        // Notes
        TextFields.Default(
            modifier = Modifier.padding(horizontal = Dimens.Normal),
            singleLine = false,
            border = false,
            padding = PaddingValues(Dimens.Small),
            placeholder = stringResource(id = R.string.placeholder_notes),
            value = workoutExercise.notes,
            onValueChange = { notes ->
                onAction(
                    ManageWorkoutUiAction.OnExerciseNotesChanged(
                        id = workoutExercise.id,
                        notes = notes
                    )
                )
            }
        )

        // Sets
        WorkoutExerciseSetTable(
            workoutExercise = workoutExercise,
            unit = unit,
            onEvent = onWorkoutEvent
        )

        // Add Set Button
        Buttons.Default(
            text = stringResource(id = R.string.button_add_set),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Normal),
            minButtonSize = 0.dp,
            contentPadding = PaddingValues(Dimens.Small),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            onClick = {
                onWorkoutEvent(WorkoutExerciseEvent.OnAddSet(workoutExercise.id))
            }
        )
    }
}

