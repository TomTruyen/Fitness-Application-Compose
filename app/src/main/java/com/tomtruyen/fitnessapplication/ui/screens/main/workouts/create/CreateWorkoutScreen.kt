package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatListNumbered
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
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
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.Toolbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@CreateWorkoutNavGraph(start = true)
@Destination
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
                    viewModel.state.value = state.copy(
                        workout = state.workout.copy(
                            exercises = state.workout.exercises + WorkoutExerciseResponse(
                                exercise = exercise,
                            ).apply { sets = listOf(WorkoutSet(workoutExerciseId = id)) }
                        )
                    )

                    navController.currentBackStackEntry?.savedStateHandle?.remove<Exercise>(NavArguments.EXERCISE)
                }
            }
    }

    LaunchedEffect(state.workout.exercises) {
        if(state.workout.exercises.isEmpty()) return@LaunchedEffect
        pagerState.animateScrollToPage(state.workout.exercises.size - 1)
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
                AnimatedVisibility(visible = state.workout.exercises.size > 1) {
                    IconButton(onClick = { onEvent(CreateWorkoutUiEvent.OnReorderExerciseClicked) }) {
                        Icon(
                            imageVector = Icons.Filled.FormatListNumbered,
                            contentDescription = stringResource(id = R.string.content_description_reorder_exercises),
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
                    TextFields.Default(
                        singleLine = false,
                        placeholder = stringResource(id = R.string.notes),
                        value = workoutExercise.notes,
                        onValueChange = { notes ->
                            onEvent(CreateWorkoutUiEvent.OnExerciseNotesChanged(index, notes))
                        },
                    )

                    Spacer(modifier = Modifier.height(Dimens.Normal))

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = Dimens.Normal)
                    ) {
                        // Header Row
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth()
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
                                        state.settings.unit
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

                        // Sets
                        items(workoutExercise.sets.size) { setIndex ->
                            val set = workoutExercise.sets.getOrNull(setIndex)

                            if(set != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
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
                                                    exerciseIndex = index,
                                                    setIndex = setIndex,
                                                    reps = reps
                                                )
                                            )
                                        },
                                        placeholder = "0",
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.NumberPassword
                                        ),
                                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                                            textAlign = TextAlign.Center
                                        ),
                                        padding = PaddingValues(Dimens.Small),
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(modifier = Modifier.width(Dimens.Small))

                                    // TODO: ADD TIME INPUT
                                    TextFields.Default(
                                        value = set.weightString ?: "",
                                        onValueChange = { weight ->
                                            val filteredWeight = weight.replace(",", ".")

                                            // Check if the number can be cast to double, if not don't update the value
                                            if(filteredWeight.isNotEmpty() && filteredWeight.toDoubleOrNull() == null) return@Default

                                            onEvent(
                                                CreateWorkoutUiEvent.OnWeightChanged(
                                                    exerciseIndex = index,
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

                                    AnimatedVisibility(visible = workoutExercise.sets.size > 1) {
                                        IconButton(
                                            modifier = Modifier.width(Dimens.MinButtonHeight),
                                            onClick = {
                                                onEvent(CreateWorkoutUiEvent.OnDeleteSetClicked(index, setIndex))
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
                        }

                        item {
                            Buttons.Text(
                                text = stringResource(id = R.string.add_set).uppercase(),
                                modifier = Modifier.fillMaxWidth()
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
        }

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
