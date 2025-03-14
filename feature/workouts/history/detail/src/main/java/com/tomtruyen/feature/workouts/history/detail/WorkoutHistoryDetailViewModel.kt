package com.tomtruyen.feature.workouts.history.detail

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.models.ManageWorkoutMode
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.data.repositories.interfaces.ExerciseRepository
import com.tomtruyen.data.repositories.interfaces.HistoryRepository
import com.tomtruyen.feature.workouts.history.detail.WorkoutHistoryDetailUiEvent.Navigate.Exercise.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class WorkoutHistoryDetailViewModel(
    id: String,
    private val historyRepository: HistoryRepository,
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

    private fun showSheet(show: Boolean) = updateState {
        it.copy(
            showSheet = show
        )
    }

    private fun toWorkout(mode: ManageWorkoutMode) {
        val workout = uiState.value.history.toWorkoutUiModel()

        triggerEvent(
            WorkoutHistoryDetailUiEvent.Navigate.Workout(
                workout = workout,
                mode = mode
            )
        )
    }

    override fun onAction(action: WorkoutHistoryDetailUiAction) {
        when(action) {
            is WorkoutHistoryDetailUiAction.Navigate.Exercise.Detail -> action.id?.let {
                triggerEvent(
                    Detail(it)
                )
            }

            WorkoutHistoryDetailUiAction.Sheet.Show -> showSheet(true)
            WorkoutHistoryDetailUiAction.Sheet.Dismiss -> showSheet(false)

            WorkoutHistoryDetailUiAction.Workout.Save -> toWorkout(ManageWorkoutMode.CREATE)
            WorkoutHistoryDetailUiAction.Workout.Start -> toWorkout(ManageWorkoutMode.EXECUTE)
        }
    }
}