package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.navigate
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.navigation.ExercisesNavGraph
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ExerciseDetailScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ExercisesFilterScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.CollapsingToolbar
import com.tomtruyen.fitnessapplication.ui.shared.ExerciseFilterChip
import com.tomtruyen.fitnessapplication.ui.shared.SearchToolbar
import com.tomtruyen.fitnessapplication.ui.shared.TextFields
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@ExercisesNavGraph(start = true)
@Destination
@Composable
fun ExercisesScreen(
    navController: NavController,
    viewModel: ExercisesViewModel
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val exercises by viewModel.exercises.collectAsStateWithLifecycle(initialValue = emptyList())

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                is ExercisesNavigationType.Filter -> navController.navigate(ExercisesFilterScreenDestination)
                is ExercisesNavigationType.Add -> TODO()
                is ExercisesNavigationType.Detail -> navController.navigate(ExerciseDetailScreenDestination(navigationType.id))
                else -> Unit
            }
        }
    }

    ExercisesScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        exercises = exercises,
        loading = loading,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ExercisesUiState,
    exercises: List<Exercise>,
    loading: Boolean,
    onEvent: (ExercisesUiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            if(state.searching) {
                SearchToolbar(
                    value = state.search,
                    onValueChange = { query ->
                        onEvent(ExercisesUiEvent.OnSearchQueryChanged(query))
                    },
                    onClose = {
                        onEvent(ExercisesUiEvent.OnSearchQueryChanged(""))
                        onEvent(ExercisesUiEvent.OnToggleSearch)
                    }
                ) {
                    IconButton(
                        onClick = {
                            onEvent(ExercisesUiEvent.OnFilterClicked)
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
                    title = stringResource(id = R.string.exercises),
                    navController = navController,
                    scrollBehavior = scrollBehavior,
                    actions = {
                        IconButton(
                            onClick = {
                                onEvent(ExercisesUiEvent.OnToggleSearch)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = stringResource(id = R.string.content_description_search)
                            )
                        }

                        IconButton(
                            onClick = {
                                onEvent(ExercisesUiEvent.OnFilterClicked)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = stringResource(id = R.string.content_description_filter)
                            )
                        }

                        IconButton(
                            onClick = {
                                onEvent(ExercisesUiEvent.OnAddClicked)
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
        // nestedScroll modifier is required for the scroll behavior to work
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        BoxWithLoader(
            loading = loading,
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                if(state.filter.categories.isNotEmpty() || state.filter.equipment.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = Dimens.Tiny),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
                    ) {
                        itemsIndexed(state.filter.categories + state.filter.equipment) { index, filter ->
                            ExerciseFilterChip(
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
                                onEvent(ExercisesUiEvent.OnRemoveFilterClicked(filter))
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(exercises) { index, exercise ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (index == 0) {
                                Divider(
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                )
                            }

                            ExerciseListItem(exercise) {
                                onEvent(ExercisesUiEvent.OnExerciseClicked(exercise))
                            }

                            if (index < exercises.lastIndex) {
                                Divider(
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseListItem(exercise: Exercise, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(Dimens.Normal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = exercise.displayName,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = exercise.category,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                ),
            )
        }

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null
        )
    }
}