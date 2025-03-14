package com.tomtruyen.feature.workouts

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.models.ManageWorkoutMode
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.BottomSheetList
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.feature.workouts.components.ActiveWorkoutBar
import com.tomtruyen.feature.workouts.components.WorkoutListItem
import com.tomtruyen.feature.workouts.remember.rememberWorkoutActions
import com.tomtruyen.navigation.Screen
import com.tomtruyen.navigation.SharedTransitionKey
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.WorkoutsScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: WorkoutsViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

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
        animatedVisibilityScope = animatedVisibilityScope,
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )

    BottomSheetList(
        items = rememberWorkoutActions(
            onAction = viewModel::onAction
        ),
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.WorkoutOverviewScreenLayout(
    snackbarHost: @Composable () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    state: WorkoutsUiState,
    onAction: (WorkoutsUiAction) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val refreshState = rememberPullToRefreshState()

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        // This will crash if we don't account for any items that we add that are not reorderable.
        // This is a dynamic way of doing it so we don't need to hardcode the amount
        val offset = lazyListState.layoutInfo.totalItemsCount - state.workouts.size

        onAction(WorkoutsUiAction.Reorder(from.index - offset, to.index - offset))
    }

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
                        imageVector = Icons.Rounded.Add,
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
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimens.Normal)
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    item {
                        Label(
                            label = stringResource(R.string.label_quick_start),
                            modifier = Modifier.padding(start = Dimens.Tiny)
                        )
                    }

                    item {
                        Buttons.Default(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.button_start_empty_workout),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                            ),
                            onClick = {
                                onAction(WorkoutsUiAction.ExecuteEmpty)
                            }
                        )
                    }

                    item {
                        Label(
                            modifier = Modifier.padding(
                                top = Dimens.Normal,
                                start = Dimens.Tiny
                            ),
                            label = stringResource(R.string.label_workouts)
                        )
                    }

                    items(
                        state.workouts,
                        key = { it.id }
                    ) { workout ->
                        ReorderableItem(
                            modifier = Modifier.padding(vertical = Dimens.Tiny),
                            state = reorderableLazyListState,
                            key = workout.id
                        ) { isDragging ->
                            val alpha by animateFloatAsState(if (isDragging) 0.25f else 1f, label = "")

                            WorkoutListItem(
                                workout = workout,
                                onAction = onAction,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .longPressDraggableHandle(
                                        onDragStarted = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        },
                                    )
                                    .alpha(alpha)
                                    .animateItem()
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(
                                            key = SharedTransitionKey.Workout(workout.id)
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}