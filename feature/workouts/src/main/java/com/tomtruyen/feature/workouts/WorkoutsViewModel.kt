package com.tomtruyen.feature.workouts

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository
) : BaseViewModel<WorkoutsUiState, WorkoutsUiAction, WorkoutsUiEvent>(
    initialState = WorkoutsUiState()
) {
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

    override fun onAction(action: WorkoutsUiAction) {
        when (action) {
            is WorkoutsUiAction.OnCreateWorkoutClicked -> triggerEvent(WorkoutsUiEvent.NavigateToManageWorkout)
            is WorkoutsUiAction.OnDetailClicked -> triggerEvent(
                WorkoutsUiEvent.NavigateToDetail(
                    action.id
                )
            )

            is WorkoutsUiAction.OnStartWorkoutClicked -> triggerEvent(
                WorkoutsUiEvent.NavigateToStartWorkout(
                    action.id
                )
            )

            WorkoutsUiAction.OnRefresh -> fetchWorkouts(refresh = true)
        }
    }
}
