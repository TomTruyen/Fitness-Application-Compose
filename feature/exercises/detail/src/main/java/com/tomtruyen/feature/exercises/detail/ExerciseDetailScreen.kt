package com.tomtruyen.feature.exercises.detail

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
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.Chip
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.tomtruyen.core.common.R as CommonR

@Composable
fun ExerciseDetailScreen(
    id: String,
    navController: NavController,
    viewModel: ExerciseDetailViewModel = koinViewModel(
        parameters = { parametersOf(id) }
    )
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is ExerciseDetailUiEvent.NavigateBack -> navController.popBackStack()
                is ExerciseDetailUiEvent.NavigateToEdit -> navController.navigate(Screen.Exercise.Manage(id))
            }
        }
    }

    ExerciseDetailScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ExerciseDetailScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: ExerciseDetailUiState,
    onAction: (ExerciseDetailUiAction) -> Unit
) {
    var confirmationDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = state.exercise?.name.orEmpty(),
                navController = navController
            ) {
                if(state.exercise?.isUserCreated == true) {
                    IconButton(
                        onClick = {
                            onAction(ExerciseDetailUiAction.Edit)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(id = CommonR.string.content_description_edit)
                        )
                    }

                    IconButton(
                        onClick = {
                            confirmationDialogVisible = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = CommonR.string.content_description_delete)
                        )
                    }
                }
            }
        }
    ) {
        LoadingContainer(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            loading = state.loading,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.exercise?.imageDetail != null || state.exercise?.image != null) {
                    item {
                        AsyncImage(
                            model = state.exercise.imageDetail ?: state.exercise.image,
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
                                state.exercise?.category,
                                state.exercise?.equipment,
                                state.exercise?.type
                            ).filter { !it.isNullOrBlank() }
                        ) { index, filter ->
                            Chip(
                                modifier = Modifier.padding(start = if (index == 0) Dimens.Normal else 0.dp),
                                text = filter?.lowercase()?.replaceFirstChar { it.uppercase() }.orEmpty(),
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

            if(confirmationDialogVisible) {
                ConfirmationDialog(
                    title = R.string.title_delete_exercise,
                    message = R.string.message_delete_exercise,
                    onConfirm = {
                        onAction(ExerciseDetailUiAction.Delete)
                        confirmationDialogVisible = false
                    },
                    onDismiss = {
                        confirmationDialogVisible = false
                    },
                    confirmText = CommonR.string.button_delete,
                )
            }
        }
    }
}