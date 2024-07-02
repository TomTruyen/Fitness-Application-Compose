package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CreateExerciseViewModel(
    private val id: String?,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository
): BaseViewModel<CreateExerciseUiState, CreateExerciseUiAction, CreateExerciseUiEvent>(
    initialState = CreateExerciseUiState(
        isEditing = id != null
    )
) {
    val categories = exerciseRepository.findCategories()
    val equipment = exerciseRepository.findEquipment().map {
        listOf(Exercise.DEFAULT_DROPDOWN_VALUE) + it
    }

    init {
        findExercise()
    }

    private fun findExercise() = launchLoading {
        if(!uiState.value.isEditing || id == null) return@launchLoading

        exerciseRepository.findUserExerciseById(id)?.let {
            updateState { state ->
                state.copy(
                    initialExercise = it,
                    exercise = it
                )
            }
        }
    }

    private fun save() = vmScope.launch {
        val userId = userRepository.getUser()?.uid ?: return@launch

        isLoading(true)

        val exercise = uiState.value.exercise.apply {
            if(equipment == Exercise.DEFAULT_DROPDOWN_VALUE) equipment = null
        }

        exerciseRepository.saveUserExercise(
            userId = userId,
            exercise = exercise,
            isUpdate = uiState.value.isEditing,
            object: FirebaseCallback<Unit> {
                override fun onSuccess(value: Unit) {
                    triggerEvent(CreateExerciseUiEvent.NavigateBack)
                }

                override fun onError(error: String?) {
                    showSnackbar(SnackbarMessage.Error(error))
                }

                override fun onStopLoading() {
                    isLoading(false)
                }
            }
        )
    }

    override fun onAction(action: CreateExerciseUiAction) {
        when(action) {
            is CreateExerciseUiAction.OnExerciseNameChanged -> updateState {
                it.copy(
                    exercise = it.exercise.copy(
                        name = action.name
                    )
                )
            }
            is CreateExerciseUiAction.OnCategoryChanged -> updateState {
                it.copy(
                    exercise = it.exercise.copy(
                        category = action.category
                    )
                )
            }
            is CreateExerciseUiAction.OnEquipmentChanged -> updateState {
                it.copy(
                    exercise = it.exercise.copy(
                        equipment = action.equipment
                    )
                )
            }
            is CreateExerciseUiAction.OnTypeChanged -> updateState {
                it.copy(
                    exercise = it.exercise.copy(
                        type = action.type
                    )
                )
            }
            is CreateExerciseUiAction.OnSaveClicked -> save()
        }
    }
}
