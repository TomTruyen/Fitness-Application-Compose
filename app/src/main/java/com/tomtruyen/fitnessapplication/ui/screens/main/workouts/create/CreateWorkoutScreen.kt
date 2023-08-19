package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.tomtruyen.fitnessapplication.navigation.CreateWorkoutNavGraph
import com.tomtruyen.fitnessapplication.navigation.NavArguments
import com.tomtruyen.fitnessapplication.networking.WorkoutExerciseResponse
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ExercisesScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ReorderWorkoutExercisesScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
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
                            exercises = state.workout.exercises + WorkoutExerciseResponse(exercise = exercise)
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
                    exercises = state.workout.exercises,
                    state = pagerState,
                    onEvent = onEvent,
                )

                Buttons.Default(
                    text = stringResource(id = R.string.add_exercise),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.Normal)
                ) {
                    onEvent(CreateWorkoutUiEvent.OnAddExerciseClicked)
                }

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
    exercises: List<WorkoutExerciseResponse>,
    state: PagerState,
    onEvent: (CreateWorkoutUiEvent) -> Unit,
) {
    HorizontalPager(
        modifier = modifier,
        state = state
    ) { index ->
        val exercise = exercises.getOrNull(index)

        if(exercise != null) {

        }

        // TODO: Display WorkoutExerciseScreen here
    }
}
