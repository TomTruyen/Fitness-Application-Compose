package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistoryWithWorkout
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.Toolbar
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination
@Composable
fun WorkoutHistoryScreen(
    navController: NavController,
    viewModel: WorkoutHistoryViewModel = koinViewModel()
) {
    val history = viewModel.history.collectAsLazyPagingItems()

    WorkoutHistoryScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        history = history
    )
}

@Composable
fun WorkoutHistoryScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    history: LazyPagingItems<WorkoutHistoryWithWorkout>
) {
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.history),
                navController = navController,
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
                .fillMaxSize()
        ) {
            items(count = history.itemCount) { index ->
                val entry = history[index]

                WorkoutHistoryItem(
                    entry = entry!!
                )
            }
        }
    }
}

@Composable
fun WorkoutHistoryItem(
    entry: WorkoutHistoryWithWorkout
) {
    Text(
        text = entry.history.id,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)
    )
}
