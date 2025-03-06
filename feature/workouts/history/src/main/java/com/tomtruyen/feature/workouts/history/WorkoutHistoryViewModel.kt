package com.tomtruyen.feature.workouts.history

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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
        uiState.distinctUntilChanged { old, new ->
            old.page == new.page
        }.map {
            it.page
        }.collectLatest { newPage ->
            historyRepository.findHistoriesAsync(newPage)
                .distinctUntilChanged()
                .collectLatest { histories ->
                    updateState {
                        // First page = overwrite, Next Pages = append
                        val newHistories = if(newPage == WorkoutHistory.INITIAL_PAGE) {
                            histories
                        } else {
                            (it.histories + histories).distinct()
                        }

                        it.copy(histories = newHistories)
                    }
                }
        }
    }

    private fun fetchWorkoutHistory(page: Int = WorkoutHistory.INITIAL_PAGE, refresh: Boolean = false) = launchLoading(refresh) {
        val userId = userRepository.getUser()?.id ?: return@launchLoading

        if(refresh) hasReachedEndOfPagination.set(false)

        updateState { it.copy(page = page) }

        if(!hasReachedEndOfPagination.get()) {
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
            WorkoutHistoryUiAction.Refresh -> fetchWorkoutHistory(
                page = WorkoutHistory.INITIAL_PAGE,
                refresh = true
            )

            is WorkoutHistoryUiAction.LoadMore -> fetchWorkoutHistory(
                page = action.page + 1
            )
        }
    }
}
