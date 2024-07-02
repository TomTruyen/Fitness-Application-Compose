package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.history

import androidx.paging.cachedIn
import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import com.tomtruyen.fitnessapplication.repositories.interfaces.WorkoutHistoryRepository

class WorkoutHistoryViewModel(
    userRepository: UserRepository,
    workoutHistoryRepository: WorkoutHistoryRepository
): BaseViewModel<WorkoutHistoryUiState, WorkoutHistoryUiAction, WorkoutHistoryUiEvent>(
    initialState = WorkoutHistoryUiState()
) {
    val history = workoutHistoryRepository.getWorkoutHistoriesPaginated(
        userRepository.getUser()?.uid.orEmpty()
    ).cachedIn(vmScope)

    override fun onAction(action: WorkoutHistoryUiAction) {
        when(action) {
            is WorkoutHistoryUiAction.OnDetailClicked -> {
                triggerEvent(WorkoutHistoryUiEvent.NavigateToDetail(action.id))
            }
        }
    }
}
