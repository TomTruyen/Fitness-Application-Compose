package com.tomtruyen.feature.workouts.history

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WorkoutHistoryViewModel(
    userRepository: UserRepository,
    workoutHistoryRepository: WorkoutHistoryRepository
) : BaseViewModel<WorkoutHistoryUiState, WorkoutHistoryUiAction, WorkoutHistoryUiEvent>(
    initialState = WorkoutHistoryUiState()
) {
    init {
        fetchWorkoutHistory()

        observeWorkoutHistory()
        observeLoading()
        observeRefreshing()
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeRefreshing() = vmScope.launch {
        refreshing.collectLatest { refreshing ->
            updateState { it.copy(refreshing = refreshing) }
        }
    }

    private fun observeWorkoutHistory = vmScope.launch {
        // TODO: Implement the collect and state update
    }

    private fun fetchWorkoutHistory(refresh: Boolean = false) = launchLoading(refresh) {
        // TODO: Implement
    }

    override fun onAction(action: WorkoutHistoryUiAction) {
        when(action) {
            WorkoutHistoryUiAction.OnRefresh -> fetchWorkoutHistory(true)
        }
    }
}
