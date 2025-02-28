package com.tomtruyen.feature.exercises

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.models.FilterOption
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Avatar
import com.tomtruyen.core.ui.Chip
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.listitems.ListItem
import com.tomtruyen.core.ui.toolbars.SearchToolbar
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.navigation.NavArguments
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ExercisesScreen(
    navController: NavController,
    viewModel: ExercisesViewModel
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is ExercisesUiEvent.NavigateToFilter -> navController.navigate(Screen.Exercise.Filter)
                is ExercisesUiEvent.NavigateToAdd -> navController.navigate(Screen.Exercise.Manage())
                is ExercisesUiEvent.NavigateToDetail -> navController.navigate(
                    Screen.Exercise.Detail(event.id)
                )
                is ExercisesUiEvent.NavigateBackToWorkout -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        NavArguments.EXERCISES,
                        state.mode to event.exercises
                    )
                    navController.popBackStack()
                }
                else -> Unit
            }
        }
    }

    ExercisesScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ExercisesUiState,
    onAction: (ExercisesUiAction) -> Unit
) {
    val refreshState = rememberPullToRefreshState()

    val filters: List<FilterOption> = remember(state.filter, state.categories) {
        state.filter.categories + state.filter.equipment
    }

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            if(state.searching) {
                SearchToolbar(
                    value = state.search,
                    onValueChange = { query ->
                        onAction(ExercisesUiAction.OnSearchQueryChanged(query))
                    },
                    onClose = {
                        onAction(ExercisesUiAction.OnSearchQueryChanged(""))
                        onAction(ExercisesUiAction.OnToggleSearch)
                    }
                ) {
                    IconButton(
                        onClick = {
                            onAction(ExercisesUiAction.OnFilterClicked)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
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
                                onAction(ExercisesUiAction.OnToggleSearch)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = stringResource(id = R.string.content_description_search)
                            )
                        }

                        IconButton(
                            onClick = {
                                onAction(ExercisesUiAction.OnFilterClicked)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = stringResource(id = R.string.content_description_filter)
                            )
                        }

                        IconButton(
                            onClick = {
                                onAction(ExercisesUiAction.OnAddClicked)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(id = R.string.content_description_create_exercise)
                            )
                        }
                    }
                )
            }
        },
        floatingActionButton = {
          AnimatedVisibility(
              visible = state.selectedExercises.isNotEmpty(),
              enter = scaleIn(),
              exit = scaleOut()
          ) {
              FloatingActionButton(
                  onClick = {
                      onAction(ExercisesUiAction.OnAddExerciseToWorkoutClicked)
                  }
              ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(id = R.string.content_description_add_exercise_to_workout)
                )
              }
          }
        },
    ) {
        LoadingContainer(
            loading = state.loading,
            modifier = Modifier.padding(it)
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
                                modifier = Modifier.padding(start = if(index == 0) Dimens.Normal else 0.dp),
                                text = filter.name,
                                selected = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onTertiary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            ) {
                                onAction(ExercisesUiAction.OnRemoveFilterClicked(filter))
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
                        onAction(ExercisesUiAction.OnRefresh)
                    },
                    state = refreshState
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        var currentLetter: Char?

                        itemsIndexed(state.exercises) { index, exercise ->
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Add Text with First Letter of Exercise Name when it changes
                                val currentFirstLetter =
                                    exercise.displayName.first().uppercaseChar()
                                val previousFirstLetter =
                                    state.exercises.getOrNull(index - 1)?.displayName?.first()
                                        ?.uppercaseChar()

                                if (currentFirstLetter != previousFirstLetter) {
                                    currentLetter = currentFirstLetter

                                    Text(
                                        text = currentLetter.toString(),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(
                                            horizontal = Dimens.Normal,
                                            vertical = Dimens.Small
                                        )
                                    )
                                }


                                ListItem(
                                    title = exercise.displayName,
                                    message = exercise.category?.name.orEmpty(),
                                    selected = state.selectedExercises.contains(exercise),
                                    showChevron = state.mode == Screen.Exercise.Overview.Mode.VIEW,
                                    onClick = {
                                        onAction(ExercisesUiAction.OnExerciseClicked(exercise))
                                    },
                                    prefix = {
                                        Avatar(
                                            imageUrl = exercise.exercise.imageUrl,
                                            contentDescription = exercise.displayName
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