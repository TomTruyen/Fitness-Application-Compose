package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.ui.screens.auth.login.LoginUiEvent
import com.tomtruyen.fitnessapplication.ui.screens.auth.login.LoginUiState
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.CollapsingToolbar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination
@Composable
fun ExercisesScreen(
    navController: NavController,
    viewModel: ExercisesViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val exercises by viewModel.exercises.collectAsStateWithLifecycle(initialValue = emptyList())

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
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
            CollapsingToolbar(
                title = stringResource(id = R.string.exercises),
                navController = navController,
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.content_description_search)
                        )
                    }

                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = stringResource(id = R.string.content_description_filter)
                        )
                    }
                }
            )
        },
        // nestedScroll modifier is required for the scroll behavior to work
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        BoxWithLoader(
            loading = loading,
            modifier = Modifier.padding(it)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                itemsIndexed(exercises) { index, exercise ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ExerciseListItem(exercise)

                        if(index < exercises.lastIndex) {
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

@Composable
fun ExerciseListItem(exercise: Exercise) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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