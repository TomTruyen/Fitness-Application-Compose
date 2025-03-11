package com.tomtruyen.feature.workouts

import android.util.Log
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import com.tomtruyen.feature.workouts.manager.DialogStateManager
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

    private val dialogStateManager by lazy {
        DialogStateManager(
            updateState = ::updateState
        )
    }

    init {
        fetchWorkouts()

        observeActiveWorkout()
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

    private fun observeActiveWorkout() = vmScope.launch {
        workoutRepository.findWorkoutByIdAsync(Workout.ACTIVE_WORKOUT_ID)
            .distinctUntilChanged()
            .collectLatest { activeWorkout ->
                updateState {
                    it.copy(
                        activeWorkout = activeWorkout
                    )
                }
            }
    }

    private fun observeWorkouts() = vmScope.launch {
        workoutRepository.findWorkoutsAsync()
            .distinctUntilChanged { old, new ->
                // Ignore changes on sortOrder to avoid flickering when reordering
                // We manually set the reorder changes in the `reorderWorkouts` function
                old.map { it.copy(sortOrder = 0) } == new.map { it.copy(sortOrder = 0) }
            }
            .collectLatest { workouts ->
                updateState {
                    it.copy(
                        workouts = workouts.filter { workout ->
                            workout.id != Workout.ACTIVE_WORKOUT_ID
                        }
                    )
                }
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

    private fun discardActiveWorkout() = launchLoading {
        workoutRepository.deleteActiveWorkout()
        onAction(WorkoutsUiAction.Dialog.Discard.Dismiss)
    }

    private fun reorderWorkouts(from: Int, to: Int) = vmScope.launch {
        val workouts = uiState.value.workouts.toMutableList().apply {
            val workout = removeAt(from)
            add(to, workout)
        }.mapIndexed { index, workout ->
            workout.copy(
                sortOrder = index,
            )
        }

        updateState {
            it.copy(
                workouts = workouts
            )
        }

        workoutRepository.reorderWorkouts(workouts)
    }

    override fun onAction(action: WorkoutsUiAction) {
        when (action) {
            is WorkoutsUiAction.Reorder -> reorderWorkouts(
                from = action.from,
                to = action.to
            )

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

            WorkoutsUiAction.ActiveWorkout.Resume -> triggerEvent(
                WorkoutsUiEvent.Navigate.Execute(Workout.ACTIVE_WORKOUT_ID)
            )

            WorkoutsUiAction.ActiveWorkout.Discard -> discardActiveWorkout()

            is WorkoutsUiAction.Dialog -> dialogStateManager.onAction(action)
            is WorkoutsUiAction.Sheet -> sheetStateManager.onAction(action)
        }
    }
}
