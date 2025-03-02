package com.tomtruyen.feature.workouts.history.detail

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.toolbars.Toolbar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WorkoutHistoryDetailScreen(
    id: String,
    navController: NavController,
    viewModel: WorkoutHistoryDetailViewModel = koinViewModel {
        parametersOf(id)
    }
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(context, viewModel) {
        viewModel.eventFlow.collectLatest { event ->
            // TODO: Implement Events
        }
    }

    WorkoutHistoryDetailScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun WorkoutHistoryDetailScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: WorkoutHistoryDetailUiState,
    onAction: (WorkoutHistoryDetailUiAction) -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            // TODO: Handle onBack when coming from Workout
            // When pressing back we don't want to go back to the Workout. We want to go to the History
            // Probably we will need to navigate popUntil in MAnageWorkoutScreen
            Toolbar(
                title = stringResource(R.string.title_history_detail),
                navController = navController
            )
        }
    ) {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
        ) {
            Text("TODO: Implement this page")
        }
    }
}

