package com.tomtruyen.fitnessapplication.ui.screens.main.workouts

import android.util.Log
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.firebase.models.WorkoutResponse
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class WorkoutOverviewViewModel(
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository
): BaseViewModel<WorkoutOverviewUiState, WorkoutOverviewUiAction, WorkoutOverviewUiEvent>(
    initialState = WorkoutOverviewUiState()
) {
    init {
        fetchWorkouts()

        observeWorkouts()
        observeLoading()
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeWorkouts() = vmScope.launch {
        workoutRepository.findWorkoutsAsync()
            .distinctUntilChanged()
            .collectLatest { workouts ->
                updateState { it.copy(workouts = workouts) }
            }
    }

    private fun fetchWorkouts() {
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

    override fun onAction(action: WorkoutOverviewUiAction) {
        when(action) {
            is WorkoutOverviewUiAction.OnCreateWorkoutClicked -> triggerEvent(WorkoutOverviewUiEvent.NavigateToCreateWorkout)
            is WorkoutOverviewUiAction.OnDetailClicked -> triggerEvent(WorkoutOverviewUiEvent.NavigateToDetail(action.id))
            is WorkoutOverviewUiAction.OnStartWorkoutClicked -> triggerEvent(WorkoutOverviewUiEvent.NavigateToStartWorkout(action.id))
        }
    }
}
