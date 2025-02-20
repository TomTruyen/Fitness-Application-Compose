package com.tomtruyen.feature.workouts.create

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.navigation.NavArguments
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.dialogs.TextFieldDialog
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.core.ui.LoadingContainer
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
fun CreateWorkoutScreen(
    navController: NavController,
    viewModel: CreateWorkoutViewModel
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is CreateWorkoutUiEvent.NavigateBack -> navController.popBackStack()
                is CreateWorkoutUiEvent.NavigateToAddExercise -> navController.navigate(Screen.Exercise.Overview(isFromWorkout = true))
            }
        }
    }

    LaunchedEffect(navController) {
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<List<Exercise>>(NavArguments.EXERCISES, emptyList())
            ?.collectLatest { exercises ->
                viewModel.onAction(CreateWorkoutUiAction.OnAddExercises(exercises))

                navController.currentBackStackEntry?.savedStateHandle?.remove<List<Exercise>>(NavArguments.EXERCISES)
            }
    }

    CreateWorkoutScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction,
        onWorkoutEvent = viewModel::onWorkoutEvent
    )
}

@Composable
fun CreateWorkoutScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: CreateWorkoutUiState,
    onAction: (CreateWorkoutUiAction) -> Unit,
    onWorkoutEvent: (WorkoutExerciseEvent) -> Unit
) {
    var workoutNameDialogVisible by remember { mutableStateOf(false) }
    var confirmationDialogVisible by remember { mutableStateOf(false) }

    BackHandler(enabled = !confirmationDialogVisible) {
        if(state.workout.exercises != state.initialWorkout.exercises) {
            confirmationDialogVisible = true
        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.title_create_workout),
                navController = navController,
                onNavigateUp = {
                    if(state.workout.exercises != state.initialWorkout.exercises) {
                        confirmationDialogVisible = true
                    } else {
                        navController.popBackStack()
                    }
                }
            ) {
                AnimatedVisibility(
                    visible = state.workout.exercises.isNotEmpty(),
                ) {
                    IconButton(
                        onClick = {
                            if (state.workout.name.isNotEmpty()) {
                                onAction(CreateWorkoutUiAction.Save(state.workout.name))
                            } else {
                                workoutNameDialogVisible = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(id = R.string.content_description_save_workout),
                        )
                    }
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

            if(workoutNameDialogVisible) {
                TextFieldDialog(
                    title = R.string.title_workout_name,
                    message = R.string.message_workout_name,
                    placeholder = R.string.title_workout_name,
                    onConfirm = { name ->
                        onAction(CreateWorkoutUiAction.Save(name))
                        workoutNameDialogVisible = false
                    },
                    onDismiss = {
                        workoutNameDialogVisible = false
                    },
                    confirmText = CommonR.string.button_save,
                )
            }
        }
    }
}

@Composable
fun ExerciseList(
    modifier: Modifier = Modifier,
    state: CreateWorkoutUiState,
    onAction: (CreateWorkoutUiAction) -> Unit,
    onWorkoutEvent: (WorkoutExerciseEvent) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onAction(CreateWorkoutUiAction.OnReorder(from.index, to.index))
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
                    .padding(Dimens.Normal)
            ) {
                onAction(CreateWorkoutUiAction.OnAddExerciseClicked)
            }
        }
    }
}

@Composable
fun ExerciseListItem(
    workoutExercise: WorkoutExerciseResponse,
    unit: String,
    onAction: (CreateWorkoutUiAction) -> Unit,
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
                // TODO: Open BottomSheet with Options
            },
        )

        // Notes
        TextFields.Default(
            modifier = Modifier.padding(horizontal = Dimens.Normal),
            singleLine = false,
            border = false,
            containerColor = Color.Transparent,
            padding = PaddingValues(Dimens.Small),
            placeholder = stringResource(id = R.string.placeholder_notes),
            value = workoutExercise.notes,
            onValueChange = { notes ->
                onAction(
                    CreateWorkoutUiAction.OnExerciseNotesChanged(
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
                onWorkoutEvent(WorkoutExerciseEvent.OnAddSetClicked(workoutExercise.id))
            }
        )
    }
}

