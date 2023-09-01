package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.base.SnackbarMessage
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import com.tomtruyen.fitnessapplication.networking.models.WorkoutResponse
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutRepository

class WorkoutOverviewViewModel(
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository
): BaseViewModel<WorkoutOverviewNavigationType>() {
    val workouts = workoutRepository.findWorkoutsAsync()

    init {
        getWorkouts()
    }

    private fun getWorkouts() {
        val userId = userRepository.getUser()?.uid ?: return

        isLoading(true)
        workoutRepository.getWorkouts(
            userId = userId,
            callback = object: FirebaseCallback<List<WorkoutResponse>> {
                override fun onError(error: String?) {
                    showSnackbar(SnackbarMessage.Error(error))
                }

                override fun onStopLoading() {
                    isLoading(false)
                }
            }
        )
    }

    fun onEvent(event: WorkoutOverviewUiEvent) {
        when(event) {
            is WorkoutOverviewUiEvent.OnHistoryClicked -> navigate(WorkoutOverviewNavigationType.History)
            is WorkoutOverviewUiEvent.OnCreateWorkoutClicked -> navigate(WorkoutOverviewNavigationType.CreateWorkout)
            is WorkoutOverviewUiEvent.OnDetailClicked -> navigate(WorkoutOverviewNavigationType.Detail(event.id))
            is WorkoutOverviewUiEvent.OnStartWorkoutClicked -> navigate(WorkoutOverviewNavigationType.StartWorkout(event.id))
        }
    }
}
