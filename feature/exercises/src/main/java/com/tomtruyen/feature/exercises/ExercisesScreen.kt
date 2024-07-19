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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.toolbars.CollapsingToolbar
import com.tomtruyen.core.ui.Chip
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.listitems.ListItem
import com.tomtruyen.core.ui.toolbars.SearchToolbar
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
                is ExercisesUiEvent.NavigateToAdd -> navController.navigate(Screen.Exercise.Create())
                is ExercisesUiEvent.NavigateToDetail -> navController.navigate(
                    Screen.Exercise.Detail(event.id)
                )
                is ExercisesUiEvent.NavigateBackToWorkout -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        NavArguments.EXERCISE,
                        event.exercise
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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val filters by remember {
        derivedStateOf {
            state.filter.categories + state.filter.equipment
        }
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
                CollapsingToolbar(
                    title = stringResource(id = R.string.title_exercises),
                    navController = navController,
                    scrollBehavior = scrollBehavior,
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
              visible = state.selectedExercise != null,
              enter = scaleIn(),
              exit = scaleOut()
          ) {
              FloatingActionButton(
                  onClick = {
                      onAction(ExercisesUiAction.OnAddExerciseToWorkoutClicked(state.selectedExercise!!))
                  }
              ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(id = R.string.content_description_add_exercise_to_workout)
                )
              }
          }
        },
        // nestedScroll modifier is required for the scroll behavior to work
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
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
                                text = filter,
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

                LazyColumn(
                    modifier = Modifier.weight(1f)
                        .animateContentSize()
                ) {
                    var currentLetter: Char?

                    itemsIndexed(state.exercises) { index, exercise ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Add Text with First Letter of Exercise Name when it changes
                            val currentFirstLetter = exercise.displayName.first().uppercaseChar()
                            val previousFirstLetter = state.exercises.getOrNull(index - 1)?.displayName?.first()?.uppercaseChar()

                            if(currentFirstLetter != previousFirstLetter) {
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
                                message = exercise.category.orEmpty(),
                                selected = state.selectedExercise?.id == exercise.id,
                                showChevron = !state.isFromWorkout,
                            ) {
                                onAction(ExercisesUiAction.OnExerciseClicked(exercise))
                            }
                        }
                    }
                }
            }
        }
    }
}