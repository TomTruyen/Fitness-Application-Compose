package com.tomtruyen.fitnessapplication.ui.shared.workout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tomtruyen.fitnessapplication.networking.models.WorkoutExerciseResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutExerciseTabLayout(
    exercises: List<WorkoutExerciseResponse>,
    state: PagerState
)  {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = state.currentPage,
        containerColor = MaterialTheme.colorScheme.background,
        edgePadding = 0.dp,
        divider = {
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp
            )
        }
    ) {
        exercises.forEachIndexed { index, workoutExercise ->
            Tab(
                selected = state.currentPage == index,
                onClick = {
                    scope.launch {
                        state.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = workoutExercise.exercise.displayName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        }
    }
}