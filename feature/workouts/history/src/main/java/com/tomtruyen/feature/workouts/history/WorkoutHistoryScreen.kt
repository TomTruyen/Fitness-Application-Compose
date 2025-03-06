package com.tomtruyen.feature.workouts.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.toolbars.Toolbar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutHistoryScreen(
    navController: NavController,
    viewModel: WorkoutHistoryViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(context, viewModel) {
        viewModel.eventFlow.collectLatest { event ->
            // TODO: Implement Events
        }
    }

    WorkoutHistoryScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
private fun WorkoutHistoryScreenLayout(
    snackbarHost: @Composable () -> Unit,
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
        snackbarHost = snackbarHost,
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
                        .padding(vertical = Dimens.Normal),
                ) {
                    // TODO: Use .animateItem() on the item that we add in here

                    itemsIndexed(
                        items = state.histories.sortedByDescending { history ->
                            history.createdAt
                        }
                    ) { index, history ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .background(
                                    color = if (index % 2 == 0) {
                                        Color.Green
                                    } else {
                                        Color.Red
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "${index} - ${history.createdAt}")
                        }

                    }
                }
            }
        }
    }
}