package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.detail

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.models.WorkoutResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow

class WorkoutDetailViewModel(
    private val id: String,
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository
): BaseViewModel<WorkoutDetailNavigationType>() {
    val state = MutableStateFlow(WorkoutDetailUiState())

    val workout = workoutRepository.findWorkoutByIdAsync(id)

    private fun delete() = launchIO {
        val userId = userRepository.getUser()?.uid ?: return@launchIO

        isLoading(true)

        workoutRepository.deleteWorkout(
            userId = userId,
            workoutId = id,
            object: FirebaseCallback<Unit> {
                override fun onSuccess(value: Unit) {
                    navigate(WorkoutDetailNavigationType.Back)
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

    fun onEvent(event: WorkoutDetailUiEvent) {
        when(event) {
            is WorkoutDetailUiEvent.Edit -> {
                navigate(WorkoutDetailNavigationType.Edit(id))
            }
            is WorkoutDetailUiEvent.Delete -> delete()
            is WorkoutDetailUiEvent.StartWorkout -> navigate(WorkoutDetailNavigationType.StartWorkout(id))
        }
    }
}