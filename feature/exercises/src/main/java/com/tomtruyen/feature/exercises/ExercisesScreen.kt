package com.tomtruyen.feature.exercises

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.ObserveEvent
import com.tomtruyen.core.common.models.ExerciseMode
import com.tomtruyen.core.common.models.FilterOption
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Avatar
import com.tomtruyen.core.ui.Chip
import com.tomtruyen.core.ui.Label
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.listitems.ListItem
import com.tomtruyen.core.ui.toolbars.SearchToolbar
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.navigation.NavResult
import com.tomtruyen.navigation.Screen
import com.tomtruyen.navigation.SharedTransitionKey
import com.tomtruyen.navigation.setNavigationResult

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ExercisesScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: ExercisesViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveEvent(viewModel) { event ->
        when (event) {
            ExercisesUiEvent.Navigate.Exercise.Filter -> navController.navigate(Screen.Exercise.Filter)
            ExercisesUiEvent.Navigate.Exercise.Add -> navController.navigate(Screen.Exercise.Manage())
            is ExercisesUiEvent.Navigate.Exercise.Detail -> navController.navigate(
                Screen.Exercise.Detail(event.id)
            )

            is ExercisesUiEvent.Navigate.Workout.Back -> {
                navController.setNavigationResult(
                    result = NavResult.ExerciseResult(
                        mode = state.mode,
                        exercises = event.exercises
                    )
                )

                navController.popBackStack()
            }

            else -> Unit
        }
    }

    ExercisesScreenLayout(
        animatedVisibilityScope = animatedVisibilityScope,
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ExercisesScreenLayout(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    state: ExercisesUiState,
    onAction: (ExercisesUiAction) -> Unit
) {
    val refreshState = rememberPullToRefreshState()

    val filters: List<FilterOption> = remember(state.filter, state.categories) {
        state.filter.categories + state.filter.equipment
    }

    Scaffold(
        modifier = Modifier.sharedBounds(
            sharedContentState = rememberSharedContentState(
                key = SharedTransitionKey.Exercise.KEY_WORKOUT_ADD_EXERCISE
            ),
            animatedVisibilityScope = animatedVisibilityScope,
        ),
        topBar = {
            AnimatedContent(
                targetState = state.searching,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) { searching ->
                if (searching) {
                    SearchToolbar(
                        value = state.search,
                        onValueChange = { query ->
                            onAction(ExercisesUiAction.Filter.OnSearchQueryChanged(query))
                        },
                        onClose = {
                            onAction(ExercisesUiAction.Filter.OnSearchQueryChanged(""))
                            onAction(ExercisesUiAction.Filter.ToggleSearch)
                        }
                    ) {
                        IconButton(
                            onClick = {
                                onAction(ExercisesUiAction.OnFilterClicked)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.FilterList,
                                contentDescription = stringResource(id = R.string.content_description_filter)
                            )
                        }
                    }
                } else {
                    Toolbar(
                        title = stringResource(id = R.string.title_exercises),
                        navController = navController,
                        actions = {
                            IconButton(
                                onClick = {
                                    onAction(ExercisesUiAction.Filter.ToggleSearch)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = stringResource(id = R.string.content_description_search),
                                )
                            }

                            IconButton(
                                onClick = {
                                    onAction(ExercisesUiAction.OnFilterClicked)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.FilterList,
                                    contentDescription = stringResource(id = R.string.content_description_filter)
                                )
                            }

                            IconButton(
                                onClick = {
                                    onAction(ExercisesUiAction.OnAddClicked)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = stringResource(id = R.string.content_description_create_exercise)
                                )
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.selectedExercises.isNotEmpty(),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        onAction(ExercisesUiAction.Workout.AddExercise)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = stringResource(id = R.string.content_description_add_exercise_to_workout)
                    )
                }
            }
        },
    ) {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                AnimatedVisibility(
                    visible = filters.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Dimens.Tiny),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
                    ) {
                        itemsIndexed(filters) { index, filter ->
                            Chip(
                                modifier = Modifier.padding(start = if (index == 0) Dimens.Normal else 0.dp),
                                text = filter.name,
                                selected = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onTertiary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            ) {
                                onAction(ExercisesUiAction.Filter.OnRemoveClicked(filter))
                            }
                        }
                    }
                }

                PullToRefreshBox(
                    modifier = Modifier
                        .weight(1f)
                        .animateContentSize(),
                    isRefreshing = state.refreshing,
                    onRefresh = {
                        onAction(ExercisesUiAction.Refresh)
                    },
                    state = refreshState
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        var currentLetter: Char?

                        itemsIndexed(
                            items = state.exercises,
                            key = { _, exercise -> exercise.id }
                        ) { index, exercise ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem()
                            ) {
                                // Add Text with First Letter of Exercise Name when it changes
                                val currentFirstLetter =
                                    exercise.displayName.firstOrNull()?.uppercaseChar()
                                val previousFirstLetter =
                                    state.exercises.getOrNull(index - 1)?.displayName?.firstOrNull()
                                        ?.uppercaseChar()

                                if (currentFirstLetter != previousFirstLetter) {
                                    currentLetter = currentFirstLetter

                                    Label(
                                        label = currentLetter.toString(),
                                        modifier = Modifier.padding(
                                            horizontal = 20.dp,
                                            vertical = Dimens.Small
                                        )
                                    )
                                }


                                ListItem(
                                    modifier = Modifier.sharedBounds(
                                        sharedContentState = rememberSharedContentState(
                                            key = SharedTransitionKey.Exercise(exercise.id)
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    ),
                                    title = exercise.displayName,
                                    message = exercise.category?.name.orEmpty(),
                                    selected = state.selectedExercises.contains(exercise),
                                    showChevron = state.mode == ExerciseMode.VIEW,
                                    onClick = {
                                        onAction(ExercisesUiAction.OnDetailClicked(exercise))
                                    },
                                    prefix = {
                                        Avatar(
                                            imageUrl = exercise.imageUrl,
                                            contentDescription = exercise.displayName,
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}