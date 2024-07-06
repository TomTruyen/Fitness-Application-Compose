package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.CollapsingToolbar
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutOverviewScreen(
    navController: NavController,
    viewModel: WorkoutOverviewViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is WorkoutOverviewUiEvent.NavigateToCreateWorkout -> navController.navigate(
                    Screen.Workout.Create()
                )
                is WorkoutOverviewUiEvent.NavigateToDetail -> navController.navigate(
                    Screen.Workout.Detail(event.id)
                )
                is WorkoutOverviewUiEvent.NavigateToStartWorkout -> navController.navigate(
                    Screen.Workout.Execute(event.id)
                )
            }
        }
    }

    WorkoutOverviewScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutOverviewScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: WorkoutOverviewUiState,
    onAction: (WorkoutOverviewUiAction) -> Unit
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
                        onAction(WorkoutOverviewUiAction.OnCreateWorkoutClicked)
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
            loading = state.loading,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
            ) {
                items(state.workouts) { workout ->
                    WorkoutListItem(
                        workoutWithExercises = workout,
                        onAction = onAction,
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
    workoutWithExercises: com.tomtruyen.data.entities.WorkoutWithExercises,
    onAction: (WorkoutOverviewUiAction) -> Unit,
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
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        onClick = {
            onAction(WorkoutOverviewUiAction.OnDetailClicked(workoutWithExercises.workout.id))
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
                        color = MaterialTheme.colorScheme.onSurface,
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
                        tint = MaterialTheme.colorScheme.onSurface,
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
                        onAction(WorkoutOverviewUiAction.OnStartWorkoutClicked(workoutWithExercises.workout.id))
                    }
                }
            }
        }
    }
}