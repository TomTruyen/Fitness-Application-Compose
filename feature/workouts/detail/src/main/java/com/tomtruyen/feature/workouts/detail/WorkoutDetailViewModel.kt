package com.tomtruyen.feature.workouts.detail

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class WorkoutDetailViewModel(
    private val id: String,
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository
) : BaseViewModel<WorkoutDetailUiState, WorkoutDetailUiAction, WorkoutDetailUiEvent>(
    initialState = WorkoutDetailUiState()
) {
    init {
        observeLoading()
        observeWorkout()
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeWorkout() = vmScope.launch {
        workoutRepository.findWorkoutByIdAsync(id)
            .filterNotNull()
            .distinctUntilChanged()
            .collectLatest { workout ->
                updateState { it.copy(workout = workout) }
            }
    }

    private fun delete() = launchLoading {
        val userId = userRepository.getUser()?.id ?: return@launchLoading

        isLoading(true)

        workoutRepository.deleteWorkout(
            userId = userId,
            workoutId = id
        )

        triggerEvent(WorkoutDetailUiEvent.NavigateBack)
    }

    override fun onAction(action: WorkoutDetailUiAction) {
        when (action) {
            is WorkoutDetailUiAction.Edit -> {
                triggerEvent(WorkoutDetailUiEvent.NavigateToEdit(id))
            }

            is WorkoutDetailUiAction.Delete -> delete()
            is WorkoutDetailUiAction.StartWorkout -> triggerEvent(
                WorkoutDetailUiEvent.NavigateToStartWorkout(
                    id
                )
            )
        }
    }
}