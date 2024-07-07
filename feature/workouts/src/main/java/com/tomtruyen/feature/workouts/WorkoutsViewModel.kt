package com.tomtruyen.feature.workouts

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.firebase.models.WorkoutResponse
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository
): BaseViewModel<WorkoutsUiState, WorkoutsUiAction, WorkoutsUiEvent>(
    initialState = WorkoutsUiState()
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

    override fun onAction(action: WorkoutsUiAction) {
        when(action) {
            is WorkoutsUiAction.OnCreateWorkoutClicked -> triggerEvent(WorkoutsUiEvent.NavigateToCreateWorkout)
            is WorkoutsUiAction.OnDetailClicked -> triggerEvent(
                WorkoutsUiEvent.NavigateToDetail(
                    action.id
                )
            )
            is WorkoutsUiAction.OnStartWorkoutClicked -> triggerEvent(
                WorkoutsUiEvent.NavigateToStartWorkout(
                    action.id
                )
            )
        }
    }
}
