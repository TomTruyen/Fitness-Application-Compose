package com.tomtruyen.feature.workouts.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.common.extensions.format
import com.tomtruyen.core.ui.toolbars.CollapsingToolbar
import com.tomtruyen.core.common.utils.TimeUtils
import com.tomtruyen.data.entities.WorkoutHistoryWithWorkout
import com.tomtruyen.feature.workouts.history.components.WorkoutHistoryItem
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutHistoryScreen(
    navController: NavController,
    viewModel: WorkoutHistoryViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val history = viewModel.history.collectAsLazyPagingItems()

    LaunchedEffect(context, viewModel) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is WorkoutHistoryUiEvent.NavigateToDetail -> {
                    // TODO: Implement -- This is for a future update
                }
            }
        }
    }

    WorkoutHistoryScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        history = history,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    history: LazyPagingItems<com.tomtruyen.data.entities.WorkoutHistoryWithWorkout>,
    onAction: (WorkoutHistoryUiAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            CollapsingToolbar(
                title = stringResource(id = R.string.title_history),
                navController = navController,
                scrollBehavior = scrollBehavior
            )
        },
        // nestedScroll modifier is required for the scroll behavior to work
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
                .fillMaxSize()
                .padding(vertical = Dimens.Normal),
        ) {
            items(count = history.itemCount) { index ->
                val entry = history[index]

                WorkoutHistoryItem(
                    entry = entry!!,
                    onAction = onAction
                )
            }
        }
    }
}