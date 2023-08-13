package com.tomtruyen.fitnessapplication.ui.screens.main.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.ui.screens.auth.login.LoginUiEvent
import com.tomtruyen.fitnessapplication.ui.screens.auth.login.LoginUiState
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
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
        state = state,
        exercises = exercises,
        loading = loading,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ExercisesScreenLayout(
    snackbarHost: @Composable () -> Unit,
    state: ExercisesUiState,
    exercises: List<Exercise>,
    loading: Boolean,
    onEvent: (ExercisesUiEvent) -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost
    ) {
        BoxWithLoader(
            loading = loading,
            modifier = Modifier.padding(it)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(exercises) { index, exercise ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ExerciseListItem(exercise)

                        if(index < exercises.lastIndex) {
                            Divider(
                                color = Color.White.copy(alpha = 0.1f),
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
            Text(text = exercise.displayName)
            Text(text = exercise.category)
        }

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null
        )
    }
}