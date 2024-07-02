package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.ExerciseRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import kotlinx.coroutines.launch

class ExerciseDetailViewModel(
    private val id: String,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository
): BaseViewModel<ExerciseDetailUiState, ExerciseDetailUiAction, ExerciseDetailUiEvent>(
    initialState = ExerciseDetailUiState()
) {
    val exercise = exerciseRepository.findExerciseById(id)

    private fun delete() = vmScope.launch {
        val userId = userRepository.getUser()?.uid ?: return@launch

        isLoading(true)

        exerciseRepository.deleteUserExercise(
            userId = userId,
            exerciseId = id,
            object: FirebaseCallback<Unit> {
                override fun onSuccess(value: Unit) {
                    triggerEvent(ExerciseDetailUiEvent.NavigateBack)
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

    override fun onAction(action: ExerciseDetailUiAction) {
        when(action) {
            is ExerciseDetailUiAction.Edit -> triggerEvent(ExerciseDetailUiEvent.NavigateToEdit(id))
            is ExerciseDetailUiAction.Delete -> delete()
        }
    }
}
