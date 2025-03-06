package com.tomtruyen.feature.workouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.models.ManageWorkoutMode
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.designsystem.theme.textButtonContentColor
import com.tomtruyen.core.ui.BottomSheetList
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.modifiers.BorderSide
import com.tomtruyen.core.ui.modifiers.directionalBorder
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.feature.workouts.components.ActiveWorkoutBar
import com.tomtruyen.feature.workouts.components.WorkoutListItem
import com.tomtruyen.feature.workouts.remember.rememberWorkoutActions
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

    val workoutActions = rememberWorkoutActions(
        onAction = viewModel::onAction
    )

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                WorkoutsUiEvent.Navigate.Create -> navController.navigate(
                    Screen.Workout.Manage(mode = ManageWorkoutMode.CREATE)
                )

                is WorkoutsUiEvent.Navigate.Edit -> navController.navigate(
                    Screen.Workout.Manage(event.id, ManageWorkoutMode.EDIT)
                )

                is WorkoutsUiEvent.Navigate.Detail -> navController.navigate(
                    Screen.Workout.Manage(event.id, ManageWorkoutMode.VIEW)
                )

                is WorkoutsUiEvent.Navigate.Execute -> navController.navigate(
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

    BottomSheetList(
        items = workoutActions,
        visible = state.showSheet,
        onDismiss = { viewModel.onAction(WorkoutsUiAction.Sheet.Dismiss) }
    )

    if (state.showDiscardConfirmation) {
        ConfirmationDialog(
            title = R.string.title_discard_workout,
            message = R.string.message_discard_workout,
            onConfirm = {
               viewModel.onAction(WorkoutsUiAction.ActiveWorkout.Discard)
            },
            onDismiss = {
                viewModel.onAction(WorkoutsUiAction.Dialog.Discard.Dismiss)
            },
            confirmText = com.tomtruyen.core.common.R.string.button_discard
        )
    }
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
                        onAction(WorkoutsUiAction.OnCreateClicked)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.content_description_create_workout)
                    )
                }
            }
        },
        bottomBar = {
            ActiveWorkoutBar(
                hasActiveWorkout = state.activeWorkout != null,
                onAction = onAction
            )
        }
    ) {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
        ) {
            PullToRefreshBox(
                isRefreshing = state.refreshing,
                onRefresh = {
                    onAction(WorkoutsUiAction.Refresh)
                },
                state = refreshState,
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimens.Normal)
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    item {
                        Label(
                            label = stringResource(R.string.label_quick_start)
                        )
                    }

                    item {
                        Buttons.Default(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.button_start_empty_workout),
                            onClick = {
                                onAction(WorkoutsUiAction.ExecuteEmpty)
                            }
                        )
                    }

                    item {
                        Label(
                            modifier = Modifier.padding(top = Dimens.Normal),
                            label = stringResource(R.string.label_workouts)
                        )
                    }

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