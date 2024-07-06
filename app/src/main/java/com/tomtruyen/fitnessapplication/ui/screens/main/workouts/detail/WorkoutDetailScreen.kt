package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.navigate
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.ui.screens.destinations.CreateWorkoutScreenDestination
import com.tomtruyen.fitnessapplication.ui.screens.destinations.ExecuteWorkoutScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.ConfirmationDialog
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.Toolbar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@RootNavGraph
@Destination
@Composable
fun WorkoutDetailScreen(
    id: String,
    navController: NavController,
    viewModel: WorkoutDetailViewModel = koinViewModel {
        parametersOf(id)
    }
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(context, viewModel) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is WorkoutDetailUiEvent.NavigateToEdit -> {
                    navController.navigate(CreateWorkoutScreenDestination(event.id))
                }
                is WorkoutDetailUiEvent.NavigateBack -> navController.popBackStack()
                is WorkoutDetailUiEvent.NavigateToStartWorkout -> {
                    navController.navigate(ExecuteWorkoutScreenDestination(event.id))
                }
            }
        }
    }

    WorkoutDetailScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun WorkoutDetailScreenLayout(
    snackbarHost : @Composable () -> Unit,
    navController: NavController,
    state: WorkoutDetailUiState,
    onAction: (WorkoutDetailUiAction) -> Unit
) {
    val workoutWithExercise by remember(state) {
        derivedStateOf { state.workout }
    }

    var confirmationDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = workoutWithExercise?.workout?.name.orEmpty(),
                navController = navController
            ) {
                IconButton(
                    onClick = {
                        onAction(WorkoutDetailUiAction.Edit)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.content_description_edit)
                    )
                }

                IconButton(
                    onClick = {
                        confirmationDialogVisible = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.content_description_delete)
                    )
                }
            }
        }
    ) {
        BoxWithLoader(
            loading = state.loading,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(Dimens.Normal)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Detail Page - Editing the Name of the workout should be done on this page, not on the edit page")
                }

                Buttons.Default(
                    text = stringResource(id = R.string.start_workout),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    onAction(WorkoutDetailUiAction.StartWorkout)
                }

                if (confirmationDialogVisible) {
                    ConfirmationDialog(
                        title = R.string.title_delete_workout,
                        message = R.string.message_delete_workout,
                        onConfirm = {
                            onAction(WorkoutDetailUiAction.Delete)
                            confirmationDialogVisible = false
                        },
                        onDismiss = {
                            confirmationDialogVisible = false
                        },
                        confirmText = R.string.delete,
                    )
                }
            }
        }
    }
}