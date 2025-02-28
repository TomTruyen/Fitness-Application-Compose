package com.tomtruyen.feature.workouts.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.feature.workouts.history.components.WorkoutHistoryItem
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun WorkoutHistoryScreen(
    navController: NavController,
    viewModel: WorkoutHistoryViewModel = koinViewModel()
) {
    val context = LocalContext.current

    // TODO: Swipe to refresh logic!

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
//        history = history,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
//    history: LazyPagingItems<com.tomtruyen.data.entities.WorkoutHistoryWithWorkout>,
    onAction: (WorkoutHistoryUiAction) -> Unit,
) {
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.title_history),
                navController = navController,
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
                .fillMaxSize()
                .padding(vertical = Dimens.Normal),
        ) {
//            items(count = history.itemCount) { index ->
//                val entry = history[index]
//
//                WorkoutHistoryItem(
//                    entry = entry!!,
//                    onAction = onAction
//                )
//            }
        }
    }
}