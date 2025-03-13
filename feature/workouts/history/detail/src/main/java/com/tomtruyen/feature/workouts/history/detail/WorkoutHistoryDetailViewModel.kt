package com.tomtruyen.feature.workouts.history.detail

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.repositories.interfaces.HistoryRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class WorkoutHistoryDetailViewModel(
    id: String,
    private val historyRepository: HistoryRepository
) : BaseViewModel<WorkoutHistoryDetailUiState, WorkoutHistoryDetailUiAction, WorkoutHistoryDetailUiEvent>(
    initialState = WorkoutHistoryDetailUiState()
) {
    init {
        observeHistory(id)

        observeLoading()
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeHistory(id: String) = vmScope.launch {
        historyRepository.findHistoryByIdAsync(id)
            .filterNotNull()
            .distinctUntilChanged()
            .collectLatest { history ->
                updateState {
                    it.copy(
                        history = history
                    )
                }
            }
    }

    override fun onAction(action: WorkoutHistoryDetailUiAction) {
        throw NotImplementedError()
    }
}