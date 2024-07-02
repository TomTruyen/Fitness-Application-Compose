package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.navigation.ExercisesNavGraph
import com.tomtruyen.fitnessapplication.ui.screens.destinations.CreateExerciseScreenDestination
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.ConfirmationDialog
import com.tomtruyen.fitnessapplication.ui.shared.ExerciseFilterChip
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.Toolbar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@ExercisesNavGraph
@Destination
@Composable
fun ExerciseDetailScreen(
    id: String,
    navController: NavController,
    viewModel: ExerciseDetailViewModel = koinViewModel(
        parameters = { parametersOf(id) }
    )
) {
    val context = LocalContext.current

    val exercise by viewModel.exercise.collectAsStateWithLifecycle(initialValue = null)
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                is ExerciseDetailNavigationType.Back -> navController.popBackStack()
                is ExerciseDetailNavigationType.Edit -> navController.navigate(CreateExerciseScreenDestination(id = id))
            }
        }
    }

    ExerciseDetailScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        exercise = exercise,
        loading = loading,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ExerciseDetailScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    exercise: Exercise?,
    loading: Boolean,
    onEvent: (ExerciseDetailUiEvent) -> Unit
) {
    var confirmationDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = exercise?.name ?: "",
                navController = navController
            ) {
                if(exercise?.isUserCreated == true) {
                    IconButton(
                        onClick = {
                            onEvent(ExerciseDetailUiEvent.Edit)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(id = R.string.content_description_edit)
                        )
                    }

                    IconButton(
                        onClick = {
                            confirmationDialogVisible = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.content_description_delete)
                        )
                    }
                }
            }
        }
    ) {
        BoxWithLoader(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            loading = loading,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (exercise?.imageDetail != null || exercise?.image != null) {
                    item {
                        AsyncImage(
                            model = exercise.imageDetail ?: exercise.image,
                            contentDescription = exercise.name,
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
                                exercise?.category,
                                exercise?.equipment,
                                exercise?.type
                            ).filter { !it.isNullOrBlank() }
                        ) { index, filter ->
                            ExerciseFilterChip(
                                modifier = Modifier.padding(start = if (index == 0) Dimens.Normal else 0.dp),
                                text = filter?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "",
                                selected = true,
                            )
                        }
                    }
                }

                if (exercise?.steps?.isNotEmpty() == true) {
                    item {
                        Text(
                            text = stringResource(id = R.string.exercise_detail_steps_title),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.W500
                            ),
                            modifier = Modifier.padding(
                                horizontal = Dimens.Normal,
                                vertical = Dimens.Tiny
                            )
                        )
                    }

                    itemsIndexed(exercise.steps) { index, step ->
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
                        onEvent(ExerciseDetailUiEvent.Delete)
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