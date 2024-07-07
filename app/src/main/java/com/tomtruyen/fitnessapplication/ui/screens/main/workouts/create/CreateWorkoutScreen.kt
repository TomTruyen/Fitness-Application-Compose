package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.outlined.Timer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.navigation.NavArguments
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseEvent
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.EmptyState
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.RestAlertDialog
import com.tomtruyen.core.ui.dialogs.TextFieldDialog
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseSet
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseSetHeader
import com.tomtruyen.core.ui.TabLayout
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreateWorkoutScreen(
    navController: NavController,
    viewModel: CreateWorkoutViewModel
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        pageCount = { state.workout.exercises.size }
    )

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is CreateWorkoutUiEvent.NavigateBack -> navController.popBackStack()
                is CreateWorkoutUiEvent.NavigateToReorderExercise -> navController.navigate(
                    Screen.Workout.ReorderExercises
                )
                is CreateWorkoutUiEvent.NavigateToAddExercise -> navController.navigate(Screen.Exercise.Overview(isFromWorkout = true))
            }
        }
    }

    LaunchedEffect(Unit, navController) {
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<com.tomtruyen.data.entities.Exercise?>(NavArguments.EXERCISE, null)
            ?.collectLatest {
                it?.let { exercise ->
                    viewModel.onAction(CreateWorkoutUiAction.OnAddExercise(exercise))

                    navController.currentBackStackEntry?.savedStateHandle?.remove<com.tomtruyen.data.entities.Exercise>(NavArguments.EXERCISE)
                }
            }
    }

    LaunchedEffect(state.selectedExerciseId) {
        if(state.workout.exercises.isEmpty()) return@LaunchedEffect

        val index = state.workout.exercises.indexOfFirst { it.exercise.id == state.selectedExerciseId }

        if(index != -1) {
            pagerState.animateScrollToPage(index)
        }
    }

    CreateWorkoutScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        pagerState = pagerState,
        onAction = viewModel::onAction,
        onWorkoutEvent = viewModel::onWorkoutEvent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateWorkoutScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: CreateWorkoutUiState,
    pagerState: PagerState,
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
                title = stringResource(id = R.string.create_workout),
                navController = navController,
                onNavigateUp = {
                    if(state.workout.exercises != state.initialWorkout.exercises) {
                        confirmationDialogVisible = true
                    } else {
                        navController.popBackStack()
                    }
                }
            ) {
                if(state.workout.exercises.size > 1) {
                    IconButton(onClick = { onAction(CreateWorkoutUiAction.OnReorderExerciseClicked) }) {
                        Icon(
                            imageVector = Icons.Filled.FormatListNumbered,
                            contentDescription = stringResource(id = R.string.content_description_reorder_exercises),
                        )
                    }
                }

                if(state.workout.exercises.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            if(state.workout.name.isNotEmpty()) {
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
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AnimatedVisibility(visible = state.workout.exercises.isNotEmpty()) {
                    TabLayout(
                        items = state.workout.exercises.map { it.exercise.displayName },
                        state = pagerState
                    )
                }

                WorkoutExerciseTabContent(
                    modifier = Modifier
                        .weight(1f)
                        .animateContentSize(),
                    state = state,
                    pagerState = pagerState,
                    onAction = onAction,
                    onWorkoutEvent = onWorkoutEvent
                )

                if(confirmationDialogVisible) {
                    ConfirmationDialog(
                        title = R.string.title_unsaved_changes,
                        message = R.string.message_unsaved_changes,
                        onConfirm = {
                            navController.popBackStack()
                            confirmationDialogVisible = false
                        },
                        onDismiss = {
                            confirmationDialogVisible = false
                        },
                        confirmText = R.string.discard
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
                        confirmText = R.string.save,
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutExerciseTabContent(
    modifier: Modifier = Modifier,
    state: CreateWorkoutUiState,
    pagerState: PagerState,
    onAction: (CreateWorkoutUiAction) -> Unit,
    onWorkoutEvent: (WorkoutExerciseEvent) -> Unit
) {
    var restDialogVisible by remember { mutableStateOf(false) }
    var deleteConfirmationDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if(state.workout.exercises.isEmpty()) {
            EmptyState(
                modifier = modifier
                    .fillMaxWidth(),
                icon = {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                            .padding(Dimens.Normal)
                    ) {
                        Icon(
                            modifier = Modifier
                                .rotate(-45f)
                                .fillMaxSize(),
                            imageVector = Icons.Filled.FitnessCenter,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                title = stringResource(id = R.string.workout_empty_title),
                subtitle = stringResource(id = R.string.workout_empty_subtitle),
            )
        } else {
            HorizontalPager(
                modifier = modifier,
                state = pagerState
            ) { index ->
                val workoutExercise = state.workout.exercises.getOrNull(index)


                if (workoutExercise != null) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .animateContentSize()
                            .padding(Dimens.Normal),
                        verticalArrangement = Arrangement.Top
                    ) {
                        item {
                            // Notes
                            TextFields.Default(
                                singleLine = false,
                                placeholder = stringResource(id = R.string.notes),
                                value = workoutExercise.notes,
                                onValueChange = { notes ->
                                    onAction(
                                        CreateWorkoutUiAction.OnExerciseNotesChanged(
                                            index,
                                            notes
                                        )
                                    )
                                },
                            )
                        }

                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {                            // Rest time
                                RestTimeSelector(
                                    workoutExercise = workoutExercise,
                                ) {
                                    restDialogVisible = true
                                }
                            }
                        }

                        item {
                        // Header Row
                            WorkoutExerciseSetHeader(
                                exercise = workoutExercise.exercise,
                                hasMultipleSets = workoutExercise.sets.size > 1,
                                unit = state.settings.unit
                            )
                        }

                        // Sets
                        itemsIndexed(workoutExercise.sets) { setIndex, set ->
                            WorkoutExerciseSet(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem(),
                                exerciseIndex = index,
                                setIndex = setIndex,
                                set = set,
                                type = workoutExercise.exercise.typeEnum,
                                hasMultipleSets = workoutExercise.sets.size > 1,
                                onEvent = onWorkoutEvent
                            )
                        }

                        item {
                            // Add Set
                            Buttons.Text(
                                text = stringResource(id = R.string.add_set).uppercase(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = Dimens.Small)
                            ) {
                                onWorkoutEvent(WorkoutExerciseEvent.OnAddSetClicked(index))
                            }
                        }
                    }
                }

                if (deleteConfirmationDialogVisible) {
                    ConfirmationDialog(
                        title = R.string.title_delete_exercise,
                        message = R.string.message_delete_exercise_from_workout,
                        onConfirm = {
                            onAction(CreateWorkoutUiAction.OnDeleteExerciseClicked(index))
                            deleteConfirmationDialogVisible = false
                        },
                        onDismiss = {
                            deleteConfirmationDialogVisible = false
                        },
                        confirmText = R.string.delete
                    )
                }

                if (restDialogVisible) {
                    RestAlertDialog(
                        onDismiss = {
                            restDialogVisible = false
                        },
                        onConfirm = { rest, restEnabled ->
                            onAction(CreateWorkoutUiAction.OnRestChanged(index, rest))

                            restEnabled?.let {
                                onAction(CreateWorkoutUiAction.OnRestEnabledChanged(index, it))
                            }
                            restDialogVisible = false
                        },
                        rest = workoutExercise?.rest ?: state.settings.rest,
                        restEnabled = workoutExercise?.restEnabled ?: state.settings.restEnabled
                    )
                }
            }
        }

        // Delete + Add Exercise row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Normal),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (state.workout.exercises.isNotEmpty()) {
                Buttons.Icon(
                    icon = Icons.Filled.Delete,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    )
                ) {
                    deleteConfirmationDialogVisible = true
                }

                Spacer(modifier = Modifier.width(Dimens.Small))
            }

            Buttons.Default(
                text = stringResource(id = R.string.add_exercise),
                modifier = Modifier
                    .weight(1f)
                    .height(IntrinsicSize.Max)
                    .animateContentSize()
            ) {
                onAction(CreateWorkoutUiAction.OnAddExerciseClicked)
            }
        }
    }
}

@Composable
fun RestTimeSelector(
    modifier: Modifier = Modifier,
    workoutExercise: WorkoutExerciseResponse,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .padding(vertical = Dimens.Normal)
            .defaultMinSize(
                minWidth = 100.dp
            )
            .animateContentSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .clickable {
                onClick()
            }
            .padding(Dimens.Normal)
            .alpha(if (workoutExercise.restEnabled) 1f else 0.5f),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Timer,
            contentDescription = null,
        )

        Text(
            text = TimeUtils.formatSeconds(workoutExercise.rest.toLong()),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = Dimens.Small)
        )
    }
}

