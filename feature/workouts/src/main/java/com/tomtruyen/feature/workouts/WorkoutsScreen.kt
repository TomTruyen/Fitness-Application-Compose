package com.tomtruyen.feature.workouts

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.models.ManageWorkoutMode
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.feature.workouts.components.WorkoutListItem
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutsScreen(
    navController: NavController,
    viewModel: WorkoutsViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is WorkoutsUiEvent.NavigateToManageWorkout -> navController.navigate(
                    Screen.Workout.Manage(mode = ManageWorkoutMode.CREATE)
                )

                is WorkoutsUiEvent.NavigateToDetail -> navController.navigate(
                    Screen.Workout.Manage(event.id, ManageWorkoutMode.VIEW)
                )

                is WorkoutsUiEvent.NavigateToStartWorkout -> navController.navigate(
                    Screen.Workout.Manage(event.id, ManageWorkoutMode.EXECUTE)
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
private fun WorkoutOverviewScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: WorkoutsUiState,
    onAction: (WorkoutsUiAction) -> Unit
) {
    val refreshState = rememberPullToRefreshState()

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.title_workouts),
                navController = navController,
            ) {
                IconButton(
                    onClick = {
                        onAction(WorkoutsUiAction.OnCreateWorkoutClicked)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.content_description_create_workout)
                    )
                }
            }
        },
    ) {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
        ) {
            PullToRefreshBox(
                isRefreshing = state.refreshing,
                onRefresh = {
                    onAction(WorkoutsUiAction.OnRefresh)
                },
                state = refreshState,
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize(),
                ) {
                    items(state.workouts) { workout ->
                        WorkoutListItem(
                            workout = workout,
                            onAction = onAction,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}