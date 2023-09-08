package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class CreateExerciseViewModel(
    private val id: String?,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository
): BaseViewModel<CreateExerciseNavigationType>() {
    private val isEditing = id != null

    val state = MutableStateFlow(
        CreateExerciseUiState(
            isEditing = isEditing
        )
    )

    val categories = exerciseRepository.findCategories()
    val equipment = exerciseRepository.findEquipment().map {
        listOf(Exercise.DEFAULT_DROPDOWN_VALUE) + it
    }

    init {
        findExercise()
    }

    private fun findExercise() = launchLoading {
        if(!isEditing || id == null) return@launchLoading

        exerciseRepository.findUserExerciseById(id)?.let {
            state.value = state.value.copy(
                initialExercise = it,
                exercise = it
            )
        }
    }

    private fun save() = launchIO {
        val userId = userRepository.getUser()?.uid ?: return@launchIO

        isLoading(true)

        val exercise = state.value.exercise.apply {
            if(equipment == Exercise.DEFAULT_DROPDOWN_VALUE) equipment = null
        }

        exerciseRepository.saveUserExercise(
            userId = userId,
            exercise = exercise,
            isUpdate = isEditing,
            object: FirebaseCallback<Unit> {
                override fun onSuccess(value: Unit) {
                    navigate(CreateExerciseNavigationType.Back)
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

    fun onEvent(event: CreateExerciseUiEvent) {
        when(event) {
            is CreateExerciseUiEvent.OnExerciseNameChanged -> {
                state.value = state.value.copy(
                    exercise = state.value.exercise.copy(
                        name = event.name
                    )
                )
            }
            is CreateExerciseUiEvent.OnCategoryChanged -> {
                state.value = state.value.copy(
                    exercise = state.value.exercise.copy(
                        category = event.category
                    )
                )
            }
            is CreateExerciseUiEvent.OnEquipmentChanged -> {
                state.value = state.value.copy(
                    exercise = state.value.exercise.copy(
                        equipment = event.equipment
                    )
                )
            }
            is CreateExerciseUiEvent.OnTypeChanged -> {
                state.value = state.value.copy(
                    exercise = state.value.exercise.copy(
                        type = event.type
                    )
                )
            }
            is CreateExerciseUiEvent.OnSaveClicked -> save()
        }
    }
}
