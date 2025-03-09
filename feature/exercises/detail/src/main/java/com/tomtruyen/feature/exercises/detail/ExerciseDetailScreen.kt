package com.tomtruyen.feature.exercises.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import com.tomtruyen.core.common.R as CommonR

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
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ExerciseDetailUiEvent.NavigateBack -> navController.popBackStack()
                is ExerciseDetailUiEvent.NavigateToEdit -> navController.navigate(
                    Screen.Exercise.Manage(
                        id
                    )
                )
            }
        }
    }

    ExerciseDetailScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
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
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ExerciseDetailScreenLayout(
    snackbarHost: @Composable () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    state: ExerciseDetailUiState,
    imageLoader: ImageLoader,
    onAction: (ExerciseDetailUiAction) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        snackbarHost = snackbarHost,
        modifier = Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(
                key = SharedTransitionKey.Exercise(state.exercise.id)
            ),
            animatedVisibilityScope = animatedVisibilityScope
        ),
        topBar = {
            Toolbar(
                title = state.exercise?.name.orEmpty(),
                navController = navController
            ) {
                if (state.exercise?.userId != null) {
                    IconButton(
                        onClick = {
                            onAction(ExerciseDetailUiAction.Sheet.Show)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
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
                if (state.exercise?.imageDetailUrl != null || state.exercise?.imageUrl != null) {
                    item {
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

                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
                    ) {
                        itemsIndexed(
                            arrayOf(
                                state.exercise?.category?.name,
                                state.exercise?.equipment?.name,
                                state.exercise?.type?.value
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

                if (state.exercise?.steps?.isNotEmpty() == true) {
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