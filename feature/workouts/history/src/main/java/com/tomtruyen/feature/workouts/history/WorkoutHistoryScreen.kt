package com.tomtruyen.feature.workouts.history

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.ObserveEvent
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.BottomSheetList
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.feature.workouts.components.WorkoutHistoryItem
import com.tomtruyen.feature.workouts.remember.rememberWorkoutHistoryActions
import com.tomtruyen.navigation.Screen
import com.tomtruyen.navigation.SharedTransitionKey
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.WorkoutHistoryScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: WorkoutHistoryViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveEvent(viewModel) { event ->
        when (event) {
            is WorkoutHistoryUiEvent.Navigate.Detail -> navController.navigate(
                Screen.History.Detail(event.id)
            )

            is WorkoutHistoryUiEvent.Navigate.Workout -> navController.navigate(
                Screen.Workout.Manage(
                    workout = event.workout,
                    mode = event.mode
                )
            )
        }
    }

    WorkoutHistoryScreenLayout(
        animatedVisibilityScope = animatedVisibilityScope,
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )

    BottomSheetList(
        items = rememberWorkoutHistoryActions(
            onAction = viewModel::onAction
        ),
        visible = state.showSheet,
        onDismiss = {
            viewModel.onAction(WorkoutHistoryUiAction.Sheet.Dismiss)
        }
    )

    if (state.showDeleteConfirmation) {
        ConfirmationDialog(
            title = R.string.title_delete_workout,
            message = R.string.message_delete_workout,
            onConfirm = {
                viewModel.onAction(WorkoutHistoryUiAction.Delete)
                viewModel.onAction(WorkoutHistoryUiAction.Dialog.Workout.Dismiss)
            },
            onDismiss = {
                viewModel.onAction(WorkoutHistoryUiAction.Dialog.Workout.Dismiss)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.WorkoutHistoryScreenLayout(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    state: WorkoutHistoryUiState,
    onAction: (WorkoutHistoryUiAction) -> Unit,
) {
    val refreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState, state.histories.size) {
        // Handle fetching paginated
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .debounce(100)
            .collect { lastVisibleItem ->
                val threshold = 2

                if (lastVisibleItem >= state.histories.size - threshold) {
                    state.histories.getOrNull(lastVisibleItem)?.let {
                        onAction(WorkoutHistoryUiAction.LoadMore(it.page))
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.title_history),
                navController = navController,
            )
        },
    ) {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
        ) {
            PullToRefreshBox(
                isRefreshing = state.refreshing,
                onRefresh = {
                    onAction(WorkoutHistoryUiAction.Refresh)
                },
                state = refreshState,
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimens.Normal)
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    item {
                        Label(
                            label = stringResource(R.string.label_workouts),
                            modifier = Modifier.padding(start = Dimens.Tiny)
                        )
                    }

                    itemsIndexed(
                        items = state.histories.sortedByDescending { history ->
                            history.createdAt
                        }
                    ) { index, history ->
                        WorkoutHistoryItem(
                            modifier = Modifier
                                .padding(vertical = Dimens.Tiny)
                                .fillMaxWidth()
                                .animateItem()
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(
                                        key = SharedTransitionKey.History(history.id)
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope
                                ),
                            history = history,
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }
}