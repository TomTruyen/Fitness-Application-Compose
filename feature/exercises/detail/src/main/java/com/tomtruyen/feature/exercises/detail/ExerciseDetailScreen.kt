package com.tomtruyen.feature.exercises.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.tomtruyen.core.common.ObserveEvent
import com.tomtruyen.core.common.utils.ImageLoader
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.BottomSheetList
import com.tomtruyen.core.ui.Chip
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.feature.exercises.detail.remember.rememberExerciseActions
import com.tomtruyen.navigation.Screen
import com.tomtruyen.navigation.SharedTransitionKey
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ExerciseDetailScreen(
    id: String,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: ExerciseDetailViewModel = koinViewModel(
        parameters = { parametersOf(id) }
    ),
    imageLoader: ImageLoader = koinInject()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveEvent(viewModel) { event ->
        when (event) {
            is ExerciseDetailUiEvent.NavigateBack -> navController.popBackStack()
            is ExerciseDetailUiEvent.NavigateToEdit -> navController.navigate(
                Screen.Exercise.Manage(
                    id
                )
            )
        }
    }

    ExerciseDetailScreenLayout(
        animatedVisibilityScope = animatedVisibilityScope,
        navController = navController,
        state = state,
        imageLoader = imageLoader,
        onAction = viewModel::onAction
    )

    BottomSheetList(
        items = rememberExerciseActions(
            onAction = viewModel::onAction
        ),
        visible = state.showSheet,
        onDismiss = {
            viewModel.onAction(ExerciseDetailUiAction.Sheet.Dismiss)
        }
    )

    if (state.showDialog) {
        ConfirmationDialog(
            title = R.string.title_delete_exercise,
            message = R.string.message_delete_exercise,
            onConfirm = {
                viewModel.onAction(ExerciseDetailUiAction.Delete)
                viewModel.onAction(ExerciseDetailUiAction.Dialog.Dismiss)
            },
            onDismiss = {
                viewModel.onAction(ExerciseDetailUiAction.Dialog.Dismiss)
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ExerciseDetailScreenLayout(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    state: ExerciseDetailUiState,
    imageLoader: ImageLoader,
    onAction: (ExerciseDetailUiAction) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(
                key = SharedTransitionKey.Exercise(state.exercise.id)
            ),
            animatedVisibilityScope = animatedVisibilityScope
        ),
        topBar = {
            Toolbar(
                title = state.exercise.name,
                navController = navController
            ) {
                if (state.exercise.userId != null) {
                    IconButton(
                        onClick = {
                            onAction(ExerciseDetailUiAction.Sheet.Show)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    ) {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.exercise.imageDetailUrl != null || state.exercise.imageUrl != null) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White),
                        ) {
                            AsyncImage(
                                model = imageLoader.load(
                                    context = context,
                                    url = state.exercise.imageDetailUrl ?: state.exercise.imageUrl
                                ),
                                contentDescription = state.exercise.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                            )
                        }
                    }
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
                    ) {
                        itemsIndexed(
                            arrayOf(
                                state.exercise.category?.name,
                                state.exercise.equipment?.name,
                                state.exercise.type.value
                            ).filter { value -> !value.isNullOrBlank() }
                        ) { index, filter ->
                            Chip(
                                modifier = Modifier.padding(start = if (index == 0) Dimens.Normal else 0.dp),
                                text = filter?.lowercase()
                                    ?.replaceFirstChar { char -> char.uppercase() }
                                    .orEmpty(),
                                selected = true,
                            )
                        }
                    }
                }

                if (state.exercise.steps.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(id = R.string.label_exercise_detail_steps_title),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.W500
                            ),
                            modifier = Modifier.padding(
                                horizontal = Dimens.Normal,
                                vertical = Dimens.Tiny
                            )
                        )
                    }

                    itemsIndexed(state.exercise.steps) { index, step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimens.Normal)
                                .padding(top = Dimens.Tiny),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Text(
                                text = "${index + 1}.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.W500
                                ),
                            )

                            Spacer(modifier = Modifier.width(Dimens.Small))

                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
    }
}