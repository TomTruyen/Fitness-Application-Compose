package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutDetailViewModel(
    private val id: String,
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository
): BaseViewModel<WorkoutDetailUiState, WorkoutDetailUiAction, WorkoutDetailUiEvent>(
    initialState = WorkoutDetailUiState()
) {
    val workout = workoutRepository.findWorkoutByIdAsync(id)

    private fun delete() = vmScope.launch {
        val userId = userRepository.getUser()?.uid ?: return@launch

        isLoading(true)

        workoutRepository.deleteWorkout(
            userId = userId,
            workoutId = id,
            object: FirebaseCallback<Unit> {
                override fun onSuccess(value: Unit) {
                    triggerEvent(WorkoutDetailUiEvent.NavigateBack)
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

    override fun onAction(action: WorkoutDetailUiAction) {
        when(action) {
            is WorkoutDetailUiAction.Edit -> {
                triggerEvent(WorkoutDetailUiEvent.NavigateToEdit(id))
            }
            is WorkoutDetailUiAction.Delete -> delete()
            is WorkoutDetailUiAction.StartWorkout -> triggerEvent(WorkoutDetailUiEvent.NavigateToStartWorkout(id))
        }
    }
}