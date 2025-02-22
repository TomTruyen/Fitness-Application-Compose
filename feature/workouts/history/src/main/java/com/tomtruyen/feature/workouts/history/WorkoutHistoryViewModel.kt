package com.tomtruyen.feature.workouts.history

import androidx.paging.cachedIn
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository

class WorkoutHistoryViewModel(
    userRepository: UserRepository,
    workoutHistoryRepository: WorkoutHistoryRepository
): BaseViewModel<WorkoutHistoryUiState, WorkoutHistoryUiAction, WorkoutHistoryUiEvent>(
    initialState = WorkoutHistoryUiState()
) {
    // TODO: Add SwipeToRefresh logic - Also see if we can improve this to use our `BaseRepository.fetch()` caching logic instead of having to fetch firebase data directly each time
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
