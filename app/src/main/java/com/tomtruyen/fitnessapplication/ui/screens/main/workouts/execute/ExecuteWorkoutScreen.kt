package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.execute

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.Toolbar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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

    val state by viewModel.state.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val workout by viewModel.workout.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(context, viewModel) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                else -> Unit
            }
        }
    }

    ExecuteWorkoutScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        loading = loading,
        workoutWithExercise = workout,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ExecuteWorkoutScreenLayout(
    snackbarHost : @Composable () -> Unit,
    navController: NavController,
    state: ExecuteWorkoutUiState,
    loading: Boolean,
    workoutWithExercise: WorkoutWithExercises?,
    onEvent: (ExecuteWorkoutUiEvent) -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = workoutWithExercise?.workout?.name ?: "",
                navController = navController
            )
        }
    ) {
        BoxWithLoader(
            loading = loading,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(text = "Execute Workout")
        }
    }
}