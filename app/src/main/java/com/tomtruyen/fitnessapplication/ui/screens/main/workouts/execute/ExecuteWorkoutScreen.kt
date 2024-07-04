package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises
import com.tomtruyen.fitnessapplication.model.StopwatchTimer
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.TextFields
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseSet
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseSetHeader
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.Toolbar
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseEvent
import com.tomtruyen.fitnessapplication.ui.shared.workout.WorkoutExerciseTabLayout
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalFoundationApi::class)
@RootNavGraph
@Destination
@Composable
fun ExecuteWorkoutScreen(
    id: String,
    navController: NavController,
    viewModel: ExecuteWorkoutViewModel = koinViewModel {
        parametersOf(id)
    }
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val stopwatchTimer = remember {
        StopwatchTimer()
    }

    val pagerState = rememberPagerState(
        pageCount = { state.workout.exercises.size },
    )

    LaunchedEffect(Unit) {
        stopwatchTimer.start()
    }

    LaunchedEffect(context, viewModel) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is ExecuteWorkoutUiEvent.NavigateToNextExercise -> {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
                is ExecuteWorkoutUiEvent.NavigateToFinish -> {
                    navController.popBackStack()
                }
            }
        }
    }

    ExecuteWorkoutScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        pagerState = pagerState,
        stopwatchTimer = stopwatchTimer,
        onAction = viewModel::onAction,
        onWorkoutEvent = viewModel::onWorkoutEvent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExecuteWorkoutScreenLayout(
    snackbarHost : @Composable () -> Unit,
    navController: NavController,
    state: ExecuteWorkoutUiState,
    pagerState: PagerState,
    stopwatchTimer: StopwatchTimer,
    onAction: (ExecuteWorkoutUiAction) -> Unit,
    onWorkoutEvent: (WorkoutExerciseEvent) -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = state.workout.name,
                navController = navController
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .animateContentSize()
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(Dimens.Small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = null,
                    )

                    Text(
                        text = stopwatchTimer.time,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = Dimens.Small)
                    )
                }

                IconButton(
                    onClick = {
                        stopwatchTimer.stop()
                        onAction(ExecuteWorkoutUiAction.FinishWorkout(stopwatchTimer.currentTime))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(id = R.string.content_description_finish_workout),
                    )
                }
            }
        }
    ) {
        BoxWithLoader(
            loading = state.loading,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AnimatedVisibility(visible = state.workout.exercises.isNotEmpty()) {
                    WorkoutExerciseTabLayout(
                        exercises = state.workout.exercises,
                        state = pagerState
                    )
                }

                WorkoutExerciseTabContent(
                    modifier = Modifier
                        .weight(1f)
                        .animateContentSize(),
                    state = state,
                    lastEntryForWorkout = state.lastEntryForWorkout,
                    pagerState = pagerState,
                    onAction = onAction,
                    onWorkoutEvent = onWorkoutEvent
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutExerciseTabContent(
    modifier: Modifier = Modifier,
    state: ExecuteWorkoutUiState,
    lastEntryForWorkout: WorkoutWithExercises?,
    pagerState: PagerState,
    onAction: (ExecuteWorkoutUiAction) -> Unit,
    onWorkoutEvent: (WorkoutExerciseEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            modifier = modifier,
            state = pagerState
        ) { index ->
            val workoutExercise = state.workout.exercises.getOrNull(index)
            val lastWorkoutExercise = lastEntryForWorkout?.exercises?.getOrNull(index)

            if (workoutExercise != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .padding(Dimens.Normal),
                    verticalArrangement = Arrangement.Top
                ) {
                    if(workoutExercise.notes.isNotEmpty()) {
                        item {
                            // Notes
                            TextFields.Default(
                                singleLine = false,
                                placeholder = stringResource(id = R.string.notes),
                                value = workoutExercise.notes,
                                onValueChange = {},
                                readOnly = true
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(Dimens.Normal))
                        }
                    }

                    item {
                        // Header Row
                        WorkoutExerciseSetHeader(
                            exercise = workoutExercise.exercise,
                            hasMultipleSets = workoutExercise.sets.size > 1,
                            isExecute = true,
                            unit = state.workout.unit
                        )
                    }

                    // Sets
                    itemsIndexed(workoutExercise.sets) { setIndex, set ->
                        val lastPerformedSet = lastWorkoutExercise?.sets?.getOrNull(setIndex)

                        WorkoutExerciseSet(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItemPlacement(),
                            exerciseIndex = index,
                            setIndex = setIndex,
                            set = set,
                            type = workoutExercise.exercise.typeEnum,
                            hasMultipleSets = workoutExercise.sets.size > 1,
                            isExecute = true,
                            lastPerformedSet = lastPerformedSet,
                            onEvent = onWorkoutEvent
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = pagerState.currentPage < pagerState.pageCount - 1,
            enter = fadeIn() + slideInHorizontally(),
            exit = slideOutHorizontally() + fadeOut()
        ) {
            Buttons.Default(
                text = stringResource(id = R.string.next_exercise),
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(Dimens.Normal),
            ) {
                onAction(ExecuteWorkoutUiAction.NextExercise)
            }
        }
    }
}
