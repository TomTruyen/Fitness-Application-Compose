package com.tomtruyen.feature.workouts

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import com.tomtruyen.feature.workouts.manager.SheetStateManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository
) : BaseViewModel<WorkoutsUiState, WorkoutsUiAction, WorkoutsUiEvent>(
    initialState = WorkoutsUiState()
) {
    private val sheetStateManager by lazy {
        SheetStateManager(
            updateState = ::updateState
        )
    }

    init {
        fetchWorkouts()

        observeWorkouts()
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

    private fun observeWorkouts() = vmScope.launch {
        workoutRepository.findWorkoutsAsync()
            .distinctUntilChanged()
            .collectLatest { workouts ->
                updateState { it.copy(workouts = workouts) }
            }
    }

    private fun fetchWorkouts(refresh: Boolean = false) = launchLoading(refresh) {
        val userId = userRepository.getUser()?.id ?: return@launchLoading

        workoutRepository.getWorkouts(
            userId = userId,
            refresh = refresh
        )
    }

    private fun delete(id: String) = launchLoading {
        workoutRepository.deleteWorkout(id)
    }

    override fun onAction(action: WorkoutsUiAction) {
        when (action) {
            WorkoutsUiAction.Refresh -> fetchWorkouts(true)

            WorkoutsUiAction.OnCreateClicked -> triggerEvent(
                WorkoutsUiEvent.Navigate.Create
            )

            WorkoutsUiAction.Edit -> uiState.value.selectedWorkoutId?.let { id ->
                triggerEvent(
                    WorkoutsUiEvent.Navigate.Edit(id)
                )
            }

            is WorkoutsUiAction.Execute -> triggerEvent(
                WorkoutsUiEvent.Navigate.Execute(action.id)
            )

            WorkoutsUiAction.ExecuteEmpty -> triggerEvent(
                WorkoutsUiEvent.Navigate.Execute(null)
            )

            is WorkoutsUiAction.OnDetailClicked -> triggerEvent(
                WorkoutsUiEvent.Navigate.Detail(
                    action.id
                )
            )

            WorkoutsUiAction.Delete -> uiState.value.selectedWorkoutId?.let(::delete)

            is WorkoutsUiAction.Sheet.Show,
            WorkoutsUiAction.Sheet.Dismiss -> sheetStateManager.onAction(action)
        }
    }
}
