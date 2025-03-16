package com.tomtruyen.feature.exercises.manage

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tomtruyen.core.common.ObserveEvent
import com.tomtruyen.core.common.models.ExerciseType
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.core.ui.Buttons
import com.tomtruyen.core.ui.FilterSelectSheet
import com.tomtruyen.core.ui.LoadingContainer
import com.tomtruyen.core.ui.SelectSheet
import com.tomtruyen.core.ui.TextFields
import com.tomtruyen.core.ui.dialogs.ConfirmationDialog
import com.tomtruyen.core.ui.toolbars.Toolbar
import com.tomtruyen.core.validation.isValid
import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.feature.exercises.create.R
import com.tomtruyen.feature.exercises.manage.model.ManageExerciseMode
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.tomtruyen.core.common.R as CommonR

@Composable
fun ManageExerciseScreen(
    id: String? = null,
    navController: NavController,
    viewModel: ManageExerciseViewModel = koinViewModel(
        parameters = { parametersOf(id) }
    )
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveEvent(viewModel) { event ->
        when (event) {
            is ManageExerciseUiEvent.Navigate.Back -> navController.popBackStack()
        }
    }

    ManageExerciseScreenLayout(
        navController = navController,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun ManageExerciseScreenLayout(
    navController: NavController,
    state: ManageExerciseUiState,
    onAction: (ManageExerciseUiAction) -> Unit,
) {
    val isValid by remember {
        derivedStateOf {
            state.nameValidationResult.isValid()
        }
    }

    val types = remember {
        ExerciseType.entries.map { it.value }
    }

    var confirmationDialogVisible by remember { mutableStateOf(false) }

    val onNavigateUp: () -> Unit = {
        if (state.exercise != state.initialExercise) {
            confirmationDialogVisible = true
        } else {
            navController.popBackStack()
        }
    }

    BackHandler(enabled = !confirmationDialogVisible, onBack = onNavigateUp)

    Scaffold(
        topBar = {
            Toolbar(
                title = stringResource(
                    id = if (state.mode == ManageExerciseMode.EDIT) {
                        R.string.title_edit_exercise
                    } else {
                        R.string.title_create_exercise
                    }
                ),
                navController = navController,
                onNavigateUp = onNavigateUp
            )
        }
    ) {
        LoadingContainer(
            loading = state.loading,
            scaffoldPadding = it
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
                    verticalArrangement = Arrangement.spacedBy(Dimens.Normal)
                ) {
                    TextFields.Default(
                        value = state.exercise.name,
                        onValueChange = { name ->
                            onAction(ManageExerciseUiAction.OnExerciseNameChanged(name))
                        },
                        withLabel = true,
                        placeholder = stringResource(id = CommonR.string.placeholder_name),
                        padding = PaddingValues(
                            horizontal = Dimens.Normal,
                            vertical = Dimens.Normal
                        )
                    )

                    FilterSelectSheet(
                        placeholder = stringResource(id = CommonR.string.placeholder_category),
                        options = state.categories,
                        selectedOption = state.exercise.category ?: CategoryUiModel.DEFAULT,
                        onOptionSelected = { category ->
                            onAction(
                                ManageExerciseUiAction.OnCategoryChanged(
                                    category
                                )
                            )
                        },
                    )

                    FilterSelectSheet(
                        placeholder = stringResource(id = CommonR.string.placeholder_equipment),
                        options = state.equipment,
                        selectedOption = state.exercise.equipment ?: EquipmentUiModel.DEFAULT,
                        onOptionSelected = { equipment ->
                            onAction(
                                ManageExerciseUiAction.OnEquipmentChanged(
                                    equipment
                                )
                            )
                        },
                    )

                    SelectSheet(
                        placeholder = stringResource(id = CommonR.string.placeholder_type),
                        options = types,
                        selectedOption = state.exercise.type.value,
                        onOptionSelected = { type ->
                            onAction(
                                ManageExerciseUiAction.OnTypeChanged(
                                    ExerciseType.fromValue(type)
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
                    onAction(ManageExerciseUiAction.Save)
                }

                if (confirmationDialogVisible) {
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