package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.tomtruyen.fitnessapplication.ui.shared.Buttons
import com.tomtruyen.fitnessapplication.ui.shared.TextFields
import com.tomtruyen.fitnessapplication.ui.shared.Toolbar
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
    val categories by viewModel.categories.collectAsStateWithLifecycle(initialValue = emptyList())
    val equipment by viewModel.equipment.collectAsStateWithLifecycle(initialValue = emptyList())

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
    categories: List<String>,
    equipment: List<String>,
    onEvent: (CreateExerciseUiEvent) -> Unit,
) {
    val types = Exercise.ExerciseType.values().map { it.name }

    var categoriesExpanded by remember { mutableStateOf(false) }
    var equipmentExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            Toolbar(
                title = stringResource(id = if(state.isEditing) R.string.edit_exercise else R.string.create_exercise),
                navController = navController,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(Dimens.Normal)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                TextFields.Default(
                    value = state.exercise.name,
                    onValueChange = { name ->
                        onEvent(CreateExerciseUiEvent.OnExerciseNameChanged(name))
                    },
                    placeholder = stringResource(id = R.string.hint_name),
                )

                DropdownMenu(
                    expanded = categoriesExpanded,
                    onDismissRequest = { categoriesExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Text(text = category)
                            },
                            onClick = {
                                onEvent(CreateExerciseUiEvent.OnCategoryChanged(category))
                                categoriesExpanded = false
                            }
                        )
                    }
                }

                DropdownMenu(
                    expanded = equipmentExpanded,
                    onDismissRequest = { equipmentExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    equipment.forEach { equipment ->
                        DropdownMenuItem(
                            text = {
                                Text(text = equipment)
                            },
                            onClick = {
                                onEvent(CreateExerciseUiEvent.OnEquipmentChanged(equipment))
                                equipmentExpanded = false
                            }
                        )
                    }
                }

                DropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    types.forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(text = type)
                            },
                            onClick = {
                                onEvent(CreateExerciseUiEvent.OnTypeChanged(type))
                                typeExpanded = false
                            }
                        )
                    }
                }
            }

            Buttons.Default(
                text = stringResource(id = R.string.save),
                modifier = Modifier.fillMaxWidth()
            ) {
                onEvent(CreateExerciseUiEvent.OnSaveClicked)
            }
        }
    }
}