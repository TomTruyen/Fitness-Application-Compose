package com.tomtruyen.feature.workouts.history

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository

class WorkoutHistoryViewModel(
    userRepository: UserRepository,
    workoutHistoryRepository: WorkoutHistoryRepository
) : BaseViewModel<WorkoutHistoryUiState, WorkoutHistoryUiAction, WorkoutHistoryUiEvent>(
    initialState = WorkoutHistoryUiState()
) {
    override fun onAction(action: WorkoutHistoryUiAction) {
        when (action) {
            is WorkoutHistoryUiAction.OnDetailClicked -> {
                triggerEvent(WorkoutHistoryUiEvent.NavigateToDetail(action.id))
            }
        }
    }
}
