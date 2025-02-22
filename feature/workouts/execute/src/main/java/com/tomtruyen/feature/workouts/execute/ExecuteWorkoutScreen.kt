package com.tomtruyen.feature.workouts.execute

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.common.utils.StopwatchTimer
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.core.ui.TabLayout
import com.tomtruyen.feature.workouts.shared.WorkoutExerciseUiAction
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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
    onWorkoutEvent: (WorkoutExerciseUiAction) -> Unit
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

//                    Text(
//                        text = stopwatchTimer.time.value,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(start = Dimens.Small)
//                    )
                }

                IconButton(
                    onClick = {
                        stopwatchTimer.stop()
//                        onAction(ExecuteWorkoutUiAction.FinishWorkout(stopwatchTimer.currentTime))
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
        LoadingContainer(
            loading = state.loading,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if(state.workout.exercises.isNotEmpty()) {
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
    lastEntryForWorkout: com.tomtruyen.data.entities.WorkoutWithExercises?,
    pagerState: PagerState,
    onAction: (ExecuteWorkoutUiAction) -> Unit,
    onWorkoutEvent: (WorkoutExerciseUiAction) -> Unit
) {
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//    ) {
//        HorizontalPager(
//            modifier = modifier,
//            state = pagerState
//        ) { index ->
//            val workoutExercise = state.workout.exercises.getOrNull(index)
//            val lastWorkoutExercise = lastEntryForWorkout?.exercises?.getOrNull(index)
//
//            if (workoutExercise != null) {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .animateContentSize()
//                        .padding(Dimens.Normal),
//                    verticalArrangement = Arrangement.Top
//                ) {
//                    if(workoutExercise.notes.isNotEmpty()) {
//                        item {
//                            // Notes
//                            TextFields.Default(
//                                singleLine = false,
//                                placeholder = stringResource(id = R.string.placeholder_notes),
//                                value = workoutExercise.notes,
//                                onValueChange = {},
//                                readOnly = true
//                            )
//                        }
//
//                        item {
//                            Spacer(modifier = Modifier.height(Dimens.Normal))
//                        }
//                    }
//
//                    item {
//                        // Header Row
//                        WorkoutExerciseSetHeader(
//                            exercise = workoutExercise.exercise,
//                            isExecute = true,
//                            unit = state.workout.unit
//                        )
//                    }
//
//                    // Sets
//                    itemsIndexed(workoutExercise.sets) { setIndex, set ->
//                        val lastPerformedSet = lastWorkoutExercise?.sets?.getOrNull(setIndex)
//
//                        WorkoutExerciseSet(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .animateItem(),
//                            exerciseIndex = index,
//                            setIndex = setIndex,
//                            set = set,
//                            type = workoutExercise.exercise.typeEnum,
//                            hasMultipleSets = workoutExercise.sets.size > 1,
//                            isExecute = true,
//                            lastPerformedSet = lastPerformedSet,
//                            onEvent = onWorkoutEvent
//                        )
//                    }
//                }
//            }
//        }
//
//        AnimatedVisibility(
//            visible = pagerState.currentPage < pagerState.pageCount - 1,
//            enter = fadeIn() + slideInHorizontally(),
//            exit = slideOutHorizontally() + fadeOut()
//        ) {
//            Buttons.Default(
//                text = stringResource(id = R.string.button_next_exercise),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .animateContentSize()
//                    .padding(Dimens.Normal),
//            ) {
//                onAction(ExecuteWorkoutUiAction.NextExercise)
//            }
//        }
//    }
}
