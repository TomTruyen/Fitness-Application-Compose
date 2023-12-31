package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

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
import com.ramcosta.composedestinations.annotation.Destination
import com.tomtruyen.fitnessapplication.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.navigation.ExercisesNavGraph
import com.tomtruyen.fitnessapplication.ui.shared.BoxWithLoader
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.dialogs.ConfirmationDialog
import com.tomtruyen.fitnessapplication.ui.shared.Dropdown
import com.tomtruyen.fitnessapplication.ui.shared.TextFields
import com.tomtruyen.fitnessapplication.ui.shared.toolbars.Toolbar
import com.tomtruyen.fitnessapplication.validation.errorMessage
import com.tomtruyen.fitnessapplication.validation.isValid
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@ExercisesNavGraph
@Destination
@Composable
fun CreateExerciseScreen(
    id: String? = null,
    navController: NavController,
    viewModel: CreateExerciseViewModel = koinViewModel(
        parameters = { parametersOf(id) }
    )
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle(initialValue = emptyList())
    val equipment by viewModel.equipment.collectAsStateWithLifecycle(initialValue = emptyList())

    LaunchedEffect(state.exercise) {
        state.validateName(context)
        state.validateCategory(context)
        state.validateType(context)
    }

    LaunchedEffect(viewModel, context) {
        viewModel.navigation.collectLatest { navigationType ->
            when(navigationType) {
                is CreateExerciseNavigationType.Back -> navController.popBackStack()
            }
        }
    }

    CreateExerciseScreenLayout(
        snackbarHost = { viewModel.CreateSnackbarHost() },
        navController = navController,
        state = state,
        loading = loading,
        categories = categories,
        equipment = equipment,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun CreateExerciseScreenLayout(
    snackbarHost: @Composable () -> Unit,
    navController: NavController,
    state: CreateExerciseUiState,
    loading: Boolean,
    categories: List<String>,
    equipment: List<String>,
    onEvent: (CreateExerciseUiEvent) -> Unit,
) {
    val isValid by remember(state) {
        derivedStateOf {
            state.nameValidationResult.isValid() &&
                    state.categoryValidationResult.isValid() &&
                    state.typeValidationResult.isValid() &&
                    state.exercise != state.initialExercise
        }
    }

    val types = Exercise.ExerciseType.values().map { it.value }
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
                title = stringResource(id = if(state.isEditing) R.string.edit_exercise else R.string.create_exercise),
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
        BoxWithLoader(
            loading = loading,
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
                            onEvent(CreateExerciseUiEvent.OnExerciseNameChanged(name))
                        },
                        placeholder = stringResource(id = R.string.hint_name),
                        padding = PaddingValues(
                            horizontal = Dimens.Normal,
                            vertical = Dimens.Tiny + Dimens.Normal
                        )
                    )

                    Dropdown(
                        placeholder = stringResource(id = R.string.hint_category),
                        options = categories,
                        selectedOption = state.exercise.category ?: "",
                        error = state.categoryValidationResult.errorMessage(),
                        onOptionSelected = { category ->
                            onEvent(
                                CreateExerciseUiEvent.OnCategoryChanged(
                                    category
                                )
                            )
                        },
                    )

                    Dropdown(
                        placeholder = stringResource(id = R.string.hint_equipment),
                        options = equipment,
                        selectedOption = state.exercise.equipment ?: equipment.firstOrNull() ?: "",
                        onOptionSelected = { equipment ->
                            onEvent(
                                CreateExerciseUiEvent.OnEquipmentChanged(
                                    equipment
                                )
                            )
                        },
                    )

                    Dropdown(
                        placeholder = stringResource(id = R.string.hint_type),
                        options = types,
                        selectedOption = state.exercise.type,
                        error = state.typeValidationResult.errorMessage(),
                        onOptionSelected = { type ->
                            onEvent(
                                CreateExerciseUiEvent.OnTypeChanged(
                                    type
                                )
                            )
                        },
                    )
                }

                Buttons.Default(
                    text = stringResource(id = R.string.save),
                    enabled = isValid,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    onEvent(CreateExerciseUiEvent.OnSaveClicked)
                }

                if(confirmationDialogVisible) {
                    ConfirmationDialog(
                        title = R.string.title_unsaved_changes,
                        message = R.string.message_unsaved_changes,
                        onConfirm = {
                            navController.popBackStack()
                            confirmationDialogVisible = false
                        },
                        onDismiss = {
                            confirmationDialogVisible = false
                        },
                        confirmText = R.string.discard
                    )
                }
            }
        }
    }
}