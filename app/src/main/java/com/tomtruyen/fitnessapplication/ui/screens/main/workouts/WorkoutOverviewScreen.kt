package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.navigate
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises
import com.tomtruyen.fitnessapplication.ui.screens.destinations.CreateWorkoutScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ExecuteWorkoutScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.WorkoutDetailScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.WorkoutHistoryScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.CollapsingToolbar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination
@Composable
fun WorkoutOverviewScreen(
    navController: NavController,
    viewModel: WorkoutOverviewViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val workouts by viewModel.workouts.collectAsStateWithLifecycle(initialValue = emptyList())
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                is WorkoutOverviewNavigationType.CreateWorkout -> navController.navigate(
                    CreateWorkoutScreenDestination(id = null)
                )
                is WorkoutOverviewNavigationType.Detail -> navController.navigate(
                    WorkoutDetailScreenDestination(navigationType.id)
                )
                is WorkoutOverviewNavigationType.StartWorkout -> navController.navigate(
                    ExecuteWorkoutScreenDestination(navigationType.id)
                )
            }
        }
    }

    WorkoutOverviewScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        workouts = workouts,
        loading = loading,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutOverviewScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    workouts: List<WorkoutWithExercises>,
    loading: Boolean,
    onEvent: (WorkoutOverviewUiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            CollapsingToolbar(
                title = stringResource(id = R.string.workouts),
                navController = navController,
                scrollBehavior = scrollBehavior
            ) {
                IconButton(
                    onClick = {
                        onEvent(WorkoutOverviewUiEvent.OnCreateWorkoutClicked)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.create_workout)
                    )
                }
            }
        },
        // nestedScroll modifier is required for the scroll behavior to work
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        BoxWithLoader(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            loading = loading,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
            ) {
                items(workouts) { workout ->
                    WorkoutListItem(
                        workoutWithExercises = workout,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutListItem(
    workoutWithExercises: WorkoutWithExercises,
    onEvent: (WorkoutOverviewUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .padding(
                horizontal = Dimens.Normal,
                vertical = Dimens.Small
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        onClick = {
            onEvent(WorkoutOverviewUiEvent.OnDetailClicked(workoutWithExercises.workout.id))
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Name + Expand Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Dimens.Normal),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = workoutWithExercises.workout.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        expanded = !expanded
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.rotate(
                            if(expanded) 180f else 0f
                        )
                    )
                }
            }

            // Exercises
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(
                        start = Dimens.Normal,
                        end = Dimens.Normal,
                        bottom = Dimens.Normal,
                        top = Dimens.Small,
                    )
                ) {
                    workoutWithExercises.exercises
                        .sortedBy { it.workoutExercise.order }
                        .forEach { exerciseWithSets ->
                        Text(
                            text = "${exerciseWithSets.sets.size} x ${exerciseWithSets.exercise.displayName}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    Buttons.Default(
                        text = stringResource(id = R.string.start_workout),
                        minButtonSize = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.Normal)
                    ) {
                        onEvent(WorkoutOverviewUiEvent.OnStartWorkoutClicked(workoutWithExercises.workout.id))
                    }
                }
            }
        }
    }
}