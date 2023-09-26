package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.history

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistoryWithWorkout
import com.tomtruyen.fitnessapplication.ui.shared.modifiers.drawColoredShadow
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.Toolbar
import com.tomtruyen.fitnessapplication.utils.TimeUtils
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination
@Composable
fun WorkoutHistoryScreen(
    navController: NavController,
    viewModel: WorkoutHistoryViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val history = viewModel.history.collectAsLazyPagingItems()

    LaunchedEffect(context, viewModel) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                is WorkoutHistoryNavigationType.Detail -> {
                    // TODO: Implement -- This is for a future update
                }
            }
        }
    }

    WorkoutHistoryScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        history = history,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun WorkoutHistoryScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    history: LazyPagingItems<WorkoutHistoryWithWorkout>,
    onEvent: (WorkoutHistoryUiEvent) -> Unit,
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
                .padding(vertical = Dimens.Normal),
        ) {
            items(count = history.itemCount) { index ->
                val entry = history[index]

                WorkoutHistoryItem(
                    entry = entry!!,
                    onEvent = onEvent
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryItem(
    entry: WorkoutHistoryWithWorkout,
    onEvent: (WorkoutHistoryUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(
                horizontal = Dimens.Normal,
                vertical = Dimens.Small
            )
            .drawColoredShadow(
                color = MaterialTheme.colorScheme.primary,
                shadowRadius = 8.dp,
                offsetY = 4.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        onClick = {
            onEvent(WorkoutHistoryUiEvent.OnDetailClicked(entry.history.id))
        }
    ) {
        // Just basic information like total weight, time
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(Dimens.Small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.workoutWithExercises.workout.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(Dimens.Small))

                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.size(Dimens.Normal)
                )

                Text(
                    text = entry.history.formattedDuration,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.padding(start = Dimens.Tiny)
                )
            }

            Text(
                text = TimeUtils.formatDate(
                    dateMillis = entry.history.createdAt
                ),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if(entry.totalWeight.toDouble() > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(Dimens.Normal)
                            .rotate(-45f)
                    )

                    Text(
                        text = "${entry.totalWeight} ${entry.weightUnit}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.weight(1f)
                            .padding(start = Dimens.Tiny)
                    )
                }
            }
        }
    }
}
