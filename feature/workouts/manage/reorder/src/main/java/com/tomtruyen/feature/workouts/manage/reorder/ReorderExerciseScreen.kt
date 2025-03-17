package com.tomtruyen.feature.workouts.manage.reorder

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.ObserveEvent
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Avatar
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.navigation.NavResult
import com.tomtruyen.navigation.setNavigationResult
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun ReorderExerciseScreen(
    navController: NavController,
    viewModel: ReorderExercisesViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveEvent(viewModel) { event ->
        when (event) {
            is ReorderExercisesUiEvent.Navigate.Submit -> {
                navController.setNavigationResult(
                    result = NavResult.ReorderExerciseResult(
                        exercises = state.exercises
                    )
                )

                navController.popBackStack()
            }
        }
    }

    ReorderExercisesScreenLayout(
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ReorderExercisesScreenLayout(
    navController: NavController,
    state: ReorderExercisesUiState,
    onAction: (ReorderExercisesUiAction) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onAction(ReorderExercisesUiAction.Reorder(from.index, to.index))
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.title_reorder_exercises),
                navController
            )
        },
        bottomBar = {
            Buttons.Default(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Dimens.Normal,
                        end = Dimens.Normal,
                        top = Dimens.Normal,
                        bottom = Dimens.Large
                    ),
                text = stringResource(id = R.string.button_done),
                onClick = {
                    onAction(ReorderExercisesUiAction.Submit)
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            state = lazyListState
        ) {
            items(
                items = state.exercises,
                key = { it.id }
            ) { exercise ->
                ReorderableItem(
                    state = reorderableLazyListState,
                    key = exercise.id,
                ) { isDragging ->
                    val alpha by animateFloatAsState(if (isDragging) 0.25f else 1f, label = "")

                    ReorderExerciseItem(
                        name = exercise.displayName,
                        imageUrl = exercise.imageUrl,
                        modifier = Modifier
                            .longPressDraggableHandle(
                                onDragStarted = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                            )
                            .alpha(alpha)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReorderExerciseItem(
    name: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.Normal,
                vertical = Dimens.Small
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        Avatar(
            imageUrl = imageUrl,
            contentDescription = name,
        )

        Text(
            modifier = Modifier.weight(1f),
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.W500
            ),
        )

        Icon(
            imageVector = Icons.Rounded.Menu,
            contentDescription = null
        )
    }
}