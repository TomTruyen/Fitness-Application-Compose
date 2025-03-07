package com.tomtruyen.feature.workouts.manage

import android.util.Log
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.models.ManageWorkoutMode
import com.tomtruyen.core.common.utils.StopwatchTimer
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import com.tomtruyen.feature.workouts.manage.manager.ExerciseStateManager
import com.tomtruyen.feature.workouts.manage.manager.SetStateManager
import com.tomtruyen.feature.workouts.manage.manager.SheetStateManager
import com.tomtruyen.feature.workouts.manage.manager.WorkoutStateManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

class ManageWorkoutViewModel(
    private val id: String?,
    mode: ManageWorkoutMode,
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository,
    private val historyRepository: WorkoutHistoryRepository,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ManageWorkoutUiState, ManageWorkoutUiAction, ManageWorkoutUiEvent>(
    initialState = ManageWorkoutUiState(
        workoutId = id,
        mode = mode
    )
) {
    private val workoutStateManager by lazy {
        WorkoutStateManager(
            updateState = ::updateState
        )
    }

    private val exerciseStateManager by lazy {
        ExerciseStateManager(
            updateState = ::updateState,
            updateStateAndGet = ::updateAndGetState,
            triggerEvent = ::triggerEvent
        )
    }

    private val setStateManager by lazy {
        SetStateManager(
            updateState = ::updateState
        )
    }
    private val sheetStateManager by lazy {
        SheetStateManager(
            updateState = ::updateState
        )
    }

    private val timer by lazy { StopwatchTimer() }

    init {
        observeWorkout()

        observeActiveWorkout()

        observeLoading()
        observeSettings()

        startTimer()
    }

    private fun startTimer() {
        if (!uiState.value.mode.isExecute) return

        timer.start()

        vmScope.launch {
            timer.time.collectLatest { duration ->
                updateState {
                    it.copy(
                        duration = duration
                    )
                }
            }
        }
    }

    private fun stopTimer() {
        if (!uiState.value.mode.isExecute) return

        timer.stop()
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeActiveWorkout() = vmScope.launch {
        if (!uiState.value.mode.isExecute) return@launch

        uiState.mapLatest {
            it.workout
        }.distinctUntilChanged().debounce(200) // Debounce as to not spam the Database
            .collectLatest { workout ->
                workoutRepository.saveActiveWorkout(workout)
            }
    }

    private fun observeWorkout() = vmScope.launch {
        if (uiState.value.mode.isCreate || id == null) return@launch

        when(uiState.value.mode) {
            ManageWorkoutMode.EXECUTE -> {
                if(id != Workout.ACTIVE_WORKOUT_ID) {
                    val workout = workoutRepository.findWorkoutById(id) ?: WorkoutUiModel()

                    workoutRepository.saveActiveWorkout(workout)
                }

                workoutRepository.findWorkoutById(Workout.ACTIVE_WORKOUT_ID)?.let { workout ->
                    updateState {
                        it.copy(
                            workout = workout,
                            initialWorkout = workout
                        )
                    }

                    fetchLatestSetsForExercises(workout)
                }
            }
            else -> {
                workoutRepository.findWorkoutByIdAsync(id)
                    .filterNotNull()
                    .distinctUntilChanged()
                    .collectLatest { workout ->
                        updateState {
                            it.copy(workout = workout)
                        }
                    }
            }
        }
    }

    private fun observeLoading() = vmScope.launch {
        loading.collectLatest { loading ->
            updateState { it.copy(loading = loading) }
        }
    }

    private fun observeSettings() = vmScope.launch {
        settingsRepository.findSettings()
            .filterNotNull()
            .distinctUntilChanged()
            .collectLatest { settings ->
                updateState { it.copy(settings = settings) }
            }
    }

    private fun fetchLatestSetsForExercises(workout: WorkoutUiModel) = vmScope.launch {
        val sets = workoutRepository.getPreviousSetsForExercises(workout)

        updateState {
            it.copy(
                previousExerciseSets = sets
            )
        }
    }

    private fun finishWorkout(userId: String) = launchLoading {
        stopTimer()

        val workoutHistoryId = historyRepository.saveWorkoutHistory(
            userId = userId,
            workout = uiState.value.workout,
            duration = timer.time.value
        )

        triggerEvent(ManageWorkoutUiEvent.Navigate.History.Detail(workoutHistoryId))
    }

    private fun saveWorkout(userId: String) = launchLoading {
        with(uiState.value) {
            workoutRepository.saveWorkout(
                userId = userId,
                workout = workout.copy(
                    unit = settings.unit,
                    name = workout.name.ifBlank { "Workout" }
                ),
            )

            triggerEvent(ManageWorkoutUiEvent.Navigate.Back)
        }
    }

    private fun save() {
        val userId = userRepository.getUser()?.id ?: return

        when (uiState.value.mode) {
            ManageWorkoutMode.EXECUTE -> finishWorkout(userId)
            else -> saveWorkout(userId)
        }
    }

    private fun delete() = launchLoading {
        id?.let { workoutId ->
            workoutRepository.deleteWorkout(workoutId)
            triggerEvent(ManageWorkoutUiEvent.Navigate.Back)
        }
    }

    private fun discard() = vmScope.launch {
        workoutRepository.deleteActiveWorkout()
        triggerEvent(ManageWorkoutUiEvent.Navigate.Back)
    }

    override fun onAction(action: ManageWorkoutUiAction) {
        when (action) {
            is ManageWorkoutUiAction.Workout -> when (action) {
                ManageWorkoutUiAction.Workout.Save -> save()
                ManageWorkoutUiAction.Workout.Delete -> delete()
                ManageWorkoutUiAction.Workout.Discard -> discard()
                else -> workoutStateManager.onAction(action)
            }

            is ManageWorkoutUiAction.Exercise -> exerciseStateManager.onAction(action)
            is ManageWorkoutUiAction.Set -> setStateManager.onAction(action)
            is ManageWorkoutUiAction.Sheet -> sheetStateManager.onAction(action)

            is ManageWorkoutUiAction.Navigate.Exercise.Detail -> triggerEvent(
                ManageWorkoutUiEvent.Navigate.Exercise.Detail(action.id)
            )

            ManageWorkoutUiAction.Navigate.Workout.Edit -> triggerEvent(
                ManageWorkoutUiEvent.Navigate.Workout.Edit(id)
            )

            ManageWorkoutUiAction.Navigate.Workout.Execute -> triggerEvent(
                ManageWorkoutUiEvent.Navigate.Workout.Execute(id)
            )
        }
    }
}