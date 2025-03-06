package com.tomtruyen.feature.workouts.history.detail

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WorkoutHistoryDetailViewModel(
    private val id: String,
    private val historyRepository: WorkoutHistoryRepository
) : BaseViewModel<WorkoutHistoryDetailUiState, WorkoutHistoryDetailUiAction, WorkoutHistoryDetailUiEvent>(
    initialState = WorkoutHistoryDetailUiState()
) {
    init {
        // TODO: Fetch the actual history from Room

        observeLoading()
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    override fun onAction(action: WorkoutHistoryDetailUiAction) {
        throw NotImplementedError()
    }
}