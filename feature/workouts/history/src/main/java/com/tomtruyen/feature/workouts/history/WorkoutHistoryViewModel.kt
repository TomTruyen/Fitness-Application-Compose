package com.tomtruyen.feature.workouts.history

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class WorkoutHistoryViewModel(
    private val userRepository: UserRepository,
    private val historyRepository: WorkoutHistoryRepository
) : BaseViewModel<WorkoutHistoryUiState, WorkoutHistoryUiAction, WorkoutHistoryUiEvent>(
    initialState = WorkoutHistoryUiState()
) {
    private var hasReachedEndOfPagination = AtomicBoolean(false)

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

    private fun observeWorkoutHistory() = vmScope.launch {
        historyRepository.findHistoriesAsync()
            .distinctUntilChanged()
            .collectLatest { histories ->
                updateState { it.copy(histories = histories) }
            }
    }

    private fun fetchWorkoutHistory(page: Int = WorkoutHistory.INITIAL_PAGE, refresh: Boolean = false) = launchLoading(refresh) {
        val userId = userRepository.getUser()?.id ?: return@launchLoading

        if(refresh) hasReachedEndOfPagination.set(false)

        if(!hasReachedEndOfPagination.get()) {
            updateState { it.copy(page = page) }

            val hasReachedEnd = historyRepository.getWorkoutHistoryPaginated(
                userId = userId,
                refresh = refresh,
                page = page
            )

            hasReachedEndOfPagination.set(hasReachedEnd)
        }
    }

    override fun onAction(action: WorkoutHistoryUiAction) {
        when(action) {
            WorkoutHistoryUiAction.OnRefresh -> fetchWorkoutHistory(
                page = WorkoutHistory.INITIAL_PAGE,
                refresh = true
            )

            is WorkoutHistoryUiAction.OnLoadMore -> fetchWorkoutHistory(
                page = action.page + 1
            )
        }
    }
}
