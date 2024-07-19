package com.tomtruyen.feature.exercises.create

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.Dropdown
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.core.validation.errorMessage
import com.tomtruyen.core.validation.isValid
import com.tomtruyen.data.entities.Exercise
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.tomtruyen.core.common.R as CommonR

@Composable
fun CreateExerciseScreen(
    id: String? = null,
    navController: NavController,
    viewModel: CreateExerciseViewModel = koinViewModel(
        parameters = { parametersOf(id) }
    )
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, context) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is CreateExerciseUiEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    CreateExerciseScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun CreateExerciseScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: CreateExerciseUiState,
    onAction: (CreateExerciseUiAction) -> Unit,
) {
    val isValid by remember(state) {
        derivedStateOf {
            state.nameValidationResult.isValid() &&
                    state.categoryValidationResult.isValid() &&
                    state.typeValidationResult.isValid() &&
                    state.exercise != state.initialExercise
        }
    }

    val types = remember {
        Exercise.ExerciseType.entries.map { it.value }
    }

    var confirmationDialogVisible by remember { mutableStateOf(false) }

    BackHandler(enabled = !confirmationDialogVisible) {
        if(state.exercise != state.initialExercise) {
            confirmationDialogVisible = true
        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(id = if(state.isEditing) R.string.title_edit_exercise else R.string.title_create_exercise),
                navController = navController,
                onNavigateUp = {
                    if(state.exercise != state.initialExercise) {
                        confirmationDialogVisible = true
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) {
        LoadingContainer(
            loading = state.loading,
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.Normal)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .animateContentSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    TextFields.Default(
                        value = state.exercise.name,
                        onValueChange = { name ->
                            onAction(CreateExerciseUiAction.OnExerciseNameChanged(name))
                        },
                        placeholder = stringResource(id = CommonR.string.placeholder_name),
                        padding = PaddingValues(
                            horizontal = Dimens.Normal,
                            vertical = Dimens.Tiny + Dimens.Normal
                        )
                    )

                    Dropdown(
                        placeholder = stringResource(id = CommonR.string.placeholder_category),
                        options = state.categories,
                        selectedOption = state.exercise.category.orEmpty(),
                        error = state.categoryValidationResult.errorMessage(),
                        onOptionSelected = { category ->
                            onAction(
                                CreateExerciseUiAction.OnCategoryChanged(
                                    category
                                )
                            )
                        },
                    )

                    Dropdown(
                        placeholder = stringResource(id = CommonR.string.placeholder_equipment),
                        options = state.equipment,
                        selectedOption = state.exercise.equipment ?: state.equipment.firstOrNull().orEmpty(),
                        onOptionSelected = { equipment ->
                            onAction(
                                CreateExerciseUiAction.OnEquipmentChanged(
                                    equipment
                                )
                            )
                        },
                    )

                    Dropdown(
                        placeholder = stringResource(id = CommonR.string.placeholder_type),
                        options = types,
                        selectedOption = state.exercise.type,
                        error = state.typeValidationResult.errorMessage(),
                        onOptionSelected = { type ->
                            onAction(
                                CreateExerciseUiAction.OnTypeChanged(
                                    type
                                )
                            )
                        },
                    )
                }

                Buttons.Default(
                    text = stringResource(id = CommonR.string.button_save),
                    enabled = isValid,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    onAction(CreateExerciseUiAction.OnSaveClicked)
                }

                if(confirmationDialogVisible) {
                    ConfirmationDialog(
                        title = CommonR.string.title_unsaved_changes,
                        message = CommonR.string.message_unsaved_changes,
                        onConfirm = {
                            navController.popBackStack()
                            confirmationDialogVisible = false
                        },
                        onDismiss = {
                            confirmationDialogVisible = false
                        },
                        confirmText = CommonR.string.button_discard
                    )
                }
            }
        }
    }
}