package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.create

import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.helpers.ContextProvider
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CreateExerciseViewModel(
    private val id: String?,
    private val contextProvider: ContextProvider,
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
    val equipment = exerciseRepository.findEquipment()

    init {
        findExercise()
    }

    private fun findExercise() = launchLoading {
        if(!isEditing || id == null) return@launchLoading

        exerciseRepository.findUserExerciseById(id)?.let {
            state.value = state.value.copy(
                exercise = it
            )
        }
    }

    private fun save() = launchIO {
        val userId = userRepository.getUser()?.uid ?: return@launchIO

        isLoading(true)

        val exercises = exerciseRepository.findUserExercises().toMutableList()

        val exercise = state.value.exercise

        if(isEditing) {
            exercises.removeIf { it.id == id }
        } else {
            exercise.id = UUID.randomUUID().toString()
        }

        exercises.add(exercise)

        exerciseRepository.saveUserExercises(
            userId = userId,
            exercises = exercises,
            object: FirebaseCallback<List<Exercise>> {
                override fun onSuccess(value: List<Exercise>) {
                    showSnackbar(
                        SnackbarMessage.Success(
                            message = contextProvider.context.getString(R.string.exercise_saved)
                        )
                    )

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
