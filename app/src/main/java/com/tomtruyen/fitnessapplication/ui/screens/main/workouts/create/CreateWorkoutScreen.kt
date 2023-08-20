package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
import com.tomtruyen.fitnessapplication.navigation.CreateWorkoutNavGraph
import com.tomtruyen.fitnessapplication.navigation.NavArguments
import com.tomtruyen.fitnessapplication.networking.WorkoutExerciseResponse
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ExercisesScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ReorderWorkoutExercisesScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.TextFields
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.ConfirmationDialog
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.RestAlertDialog
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.TextFieldDialog
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.Toolbar
import com.tomtruyen.fitnessapplication.utils.TimeUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@CreateWorkoutNavGraph(start = true)
@Destination(
    navArgsDelegate = CreateWorkoutNavArgsDelegate::class,
)
@Composable
fun CreateWorkoutScreen(
    navController: NavController,
    viewModel: CreateWorkoutViewModel
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle(initialValue = null)
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        pageCount = { state.workout.exercises.size }
    )

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                is CreateWorkoutNavigationType.Back -> navController.popBackStack()
                is CreateWorkoutNavigationType.ReorderExercise -> navController.navigate(
                    ReorderWorkoutExercisesScreenDestination
                )
                is CreateWorkoutNavigationType.AddExercise -> navController.navigate(ExercisesScreenDestination(isFromWorkout = true))
            }
        }
    }

    LaunchedEffect(Unit, navController) {
        navController.currentBackStackEntry?.savedStateHandle?.getStateFlow<Exercise?>(NavArguments.EXERCISE, null)
            ?.collectLatest {
                it?.let { exercise ->
                    viewModel.onEvent(CreateWorkoutUiEvent.OnAddExercise(exercise))

                    navController.currentBackStackEntry?.savedStateHandle?.remove<Exercise>(NavArguments.EXERCISE)
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

    LaunchedEffect(settings) {
        if(settings != null) {
            viewModel.onEvent(CreateWorkoutUiEvent.OnSettingsChanged(settings))
        }
    }

    CreateWorkoutScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        loading = loading,
        pagerState = pagerState,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateWorkoutScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: CreateWorkoutUiState,
    loading: Boolean,
    pagerState: PagerState,
    onEvent: (CreateWorkoutUiEvent) -> Unit,
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
                    IconButton(onClick = { onEvent(CreateWorkoutUiEvent.OnReorderExerciseClicked) }) {
                        Icon(
                            imageVector = Icons.Filled.FormatListNumbered,
                            contentDescription = stringResource(id = R.string.content_description_reorder_exercises),
                        )
                    }
                }

                if(state.workout.exercises.isNotEmpty()) {
                    IconButton(onClick = { workoutNameDialogVisible = true }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(id = R.string.content_description_save_workout),
                        )
                    }
                }
            }
        }
    ) {
        BoxWithLoader(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            loading = loading,
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.workout.exercises.isNotEmpty()) {
                    TabLayout(
                        exercises = state.workout.exercises,
                        state = pagerState
                    )
                }

                TabContentPager(
                    modifier = Modifier.weight(1f),
                    state = state,
                    pagerState = pagerState,
                    onEvent = onEvent,
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
                        onConfirm = { name ->
                            onEvent(CreateWorkoutUiEvent.Save(name))
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
fun TabLayout(
    exercises: List<WorkoutExerciseResponse>,
    state: PagerState
)  {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = state.currentPage,
        containerColor = MaterialTheme.colorScheme.background,
        edgePadding = 0.dp,
        divider = {
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp
            )
        }
    ) {
        exercises.forEachIndexed { index, workoutExercise ->
            Tab(
                selected = state.currentPage == index,
                onClick = {
                    scope.launch {
                        state.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = workoutExercise.exercise.displayName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabContentPager(
    modifier: Modifier = Modifier,
    state: CreateWorkoutUiState,
    pagerState: PagerState,
    onEvent: (CreateWorkoutUiEvent) -> Unit,
) {
    var restDialogVisible by remember { mutableStateOf(false) }
    var deleteConfirmationDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            modifier = modifier,
            state = pagerState
        ) { index ->
            val workoutExercise = state.workout.exercises.getOrNull(index)


            if (workoutExercise != null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(Dimens.Normal),
                    verticalArrangement = Arrangement.Top
                ) {
                    // Notes
                    TextFields.Default(
                        singleLine = false,
                        placeholder = stringResource(id = R.string.notes),
                        value = workoutExercise.notes,
                        onValueChange = { notes ->
                            onEvent(CreateWorkoutUiEvent.OnExerciseNotesChanged(index, notes))
                        },
                    )

                    // Rest time
                    RestTimeSelector(
                        modifier = Modifier.align(Alignment.End),
                        workoutExercise = workoutExercise,
                    ) {
                        restDialogVisible = true
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = Dimens.Normal)
                    ) {
                        // Header Row
                        item {
                            WorkoutExerciseSetHeader(
                                workoutExercise = workoutExercise,
                                unit = state.settings.unit
                            )
                        }

                        // Sets
                        itemsIndexed(workoutExercise.sets, key = { _, set -> set.id }) { setIndex, set ->
                            WorkoutExerciseSet(
                                modifier = Modifier.fillMaxWidth()
                                    .animateItemPlacement(),
                                exerciseIndex = index,
                                setIndex = setIndex,
                                set = set,
                                isOnlySet = workoutExercise.sets.size == 1,
                                onEvent = onEvent
                            )
                        }

                        // Add Set
                        item {
                            Buttons.Text(
                                text = stringResource(id = R.string.add_set).uppercase(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = Dimens.Small)
                            ) {
                                onEvent(CreateWorkoutUiEvent.OnAddSetClicked(index))
                            }
                        }
                    }
               }
            }

            if (deleteConfirmationDialogVisible) {
                ConfirmationDialog(
                    title = R.string.title_delete_exercise,
                    message = R.string.message_delete_exercise_from_workout,
                    onConfirm = {
                        onEvent(CreateWorkoutUiEvent.OnDeleteExerciseClicked(index))
                        deleteConfirmationDialogVisible = false
                    },
                    onDismiss = {
                        deleteConfirmationDialogVisible = false
                    },
                    confirmText = R.string.delete
                )
            }

            if(restDialogVisible) {
                RestAlertDialog(
                    onDismiss = {
                        restDialogVisible = false
                    },
                    onConfirm = { rest, restEnabled ->
                        onEvent(CreateWorkoutUiEvent.OnRestChanged(index, rest))

                        restEnabled?.let {
                            onEvent(CreateWorkoutUiEvent.OnRestEnabledChanged(index, it))
                        }
                        restDialogVisible = false
                    },
                    rest = workoutExercise?.rest ?: state.settings.rest,
                    restEnabled = workoutExercise?.restEnabled ?: state.settings.restEnabled
                )
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
            ) {
                onEvent(CreateWorkoutUiEvent.OnAddExerciseClicked)
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
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onClick()
            }
            .padding(Dimens.Normal)
            .alpha(if(workoutExercise.restEnabled) 1f else 0.5f),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Timer,
            contentDescription = null,
        )

        Text(
            text = TimeUtils.formatSeconds(workoutExercise.rest),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = Dimens.Small)
        )
    }
}

@Composable
fun WorkoutExerciseSetHeader(
    workoutExercise: WorkoutExerciseResponse,
    unit: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.Tiny),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.set).uppercase(),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(Dimens.MinButtonHeight),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W500,
            )
        )

        Text(
            text = stringResource(id = R.string.reps).uppercase(),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W500,
            )
        )

        Spacer(modifier = Modifier.width(Dimens.Small))

        Text(
            text = if(workoutExercise.exercise.typeEnum == Exercise.ExerciseType.WEIGHT) {
                unit
            } else {
                stringResource(id = R.string.time)
            }.uppercase(),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.W500,
            )
        )

        AnimatedVisibility(visible = workoutExercise.sets.size > 1) {
            Spacer(modifier = Modifier.width(Dimens.MinButtonHeight))
        }
    }
}

@Composable
fun WorkoutExerciseSet(
    modifier: Modifier = Modifier,
    exerciseIndex: Int,
    setIndex: Int,
    set: WorkoutSet,
    isOnlySet: Boolean = false,
    onEvent: (CreateWorkoutUiEvent) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${setIndex + 1}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.primary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(Dimens.MinButtonHeight)
        )

        TextFields.Default(
            value = set.repsString ?: "",
            onValueChange = { reps ->
                // Check if value can be cast to int, if not don't update the value
                if(reps.isNotEmpty() && reps.toIntOrNull() == null) return@Default

                onEvent(
                    CreateWorkoutUiEvent.OnRepsChanged(
                        exerciseIndex = exerciseIndex,
                        setIndex = setIndex,
                        reps = reps
                    )
                )
            },
            placeholder = "0",
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Next
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center
            ),
            padding = PaddingValues(Dimens.Small),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(Dimens.Small))

        TextFields.Default(
            value = set.weightString ?: "",
            onValueChange = { weight ->
                val filteredWeight = weight.replace(",", ".")

                // Check if the number can be cast to double, if not don't update the value
                if(filteredWeight.isNotEmpty() && filteredWeight.toDoubleOrNull() == null) return@Default

                onEvent(
                    CreateWorkoutUiEvent.OnWeightChanged(
                        exerciseIndex = exerciseIndex,
                        setIndex = setIndex,
                        weight = filteredWeight
                    )
                )
            },
            placeholder = "0",
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center
            ),
            padding = PaddingValues(Dimens.Small),
            modifier = Modifier.weight(1f)
        )

        AnimatedVisibility(visible = !isOnlySet) {
            IconButton(
                modifier = Modifier.width(Dimens.MinButtonHeight),
                onClick = {
                    onEvent(CreateWorkoutUiEvent.OnDeleteSetClicked(exerciseIndex, setIndex))
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null
                )
            }
        }
    }
}