package com.tomtruyen.feature.workouts.manage

import android.util.Log
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.models.WorkoutMode
import com.tomtruyen.core.common.utils.StopwatchTimer
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.models.extensions.copyFromActiveWorkout
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.data.repositories.interfaces.HistoryRepository
import com.tomtruyen.data.repositories.interfaces.PreviousSetRepository
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import com.tomtruyen.feature.workouts.manage.manager.DialogStateManager
import com.tomtruyen.feature.workouts.manage.manager.ExerciseStateManager
import com.tomtruyen.feature.workouts.manage.manager.NavResultStateManager
import com.tomtruyen.feature.workouts.manage.manager.SetStateManager
import com.tomtruyen.feature.workouts.manage.manager.SheetStateManager
import com.tomtruyen.feature.workouts.manage.manager.WorkoutStateManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

class ManageWorkoutViewModel(
    private val id: String?,
    mode: WorkoutMode,
    workout: WorkoutUiModel?, // Workout passed from History
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository,
    private val historyRepository: HistoryRepository,
    private val previousSetRepository: PreviousSetRepository,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ManageWorkoutUiState, ManageWorkoutUiAction, ManageWorkoutUiEvent>(
    initialState = ManageWorkoutUiState(
        workoutId = id,
        mode = mode,
        workout = workout ?: WorkoutUiModel()
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

    private val navResultStateManager by lazy {
        NavResultStateManager(
            updateState = ::updateState,
            onExerciseAction = ::onAction
        )
    }

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

    private var observeActiveWorkoutJob: Job? = null
    private lateinit var timer: StopwatchTimer

    init {
        observeWorkout()
        observeActiveWorkout()

        if (mode.isExecute) {
            fetchLatestSetsForExercises()
        }

        observeLoading()
        observeSettings()
    }

    private fun startTimer(initialTime: Long) {
        if (!uiState.value.mode.isExecute) return

        timer = StopwatchTimer(initialTime).also {
            it.start()
        }

        vmScope.launch {
            timer.time.collectLatest { duration ->
                updateState {
                    it.copy(
                        workout = it.workout.copy(
                            duration = duration
                        )
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
    private fun observeActiveWorkout() {
        observeActiveWorkoutJob = vmScope.launch {
            if (!uiState.value.mode.isExecute) return@launch

            uiState.mapLatest {
                it.workout
            }.distinctUntilChanged().debounce(200) // Debounce as to not spam the Database
                .collectLatest { workout ->
                    workoutRepository.saveActiveWorkout(workout)
                }
        }
    }

    private fun observeWorkout() = with(uiState.value) {
        vmScope.launch {
            if (mode.isExecute && id == null) {
                // "Start Empty Workout" -> Start Timer for it
                startTimer(0L)
            }

            if (mode.isCreate || id == null) return@launch

            when (mode) {
                WorkoutMode.VIEW -> {
                    workoutRepository.findWorkoutByIdAsync(id)
                        .filterNotNull()
                        .distinctUntilChanged()
                        .collectLatest { workout ->
                            updateState {
                                it.copy(workout = workout)
                            }
                        }
                }

                else -> {
                    val workoutId = if (mode.isExecute && id != Workout.ACTIVE_WORKOUT_ID) {
                        val workout = workoutRepository.findWorkoutById(id) ?: WorkoutUiModel()

                        workoutRepository.saveActiveWorkout(workout)

                        Workout.ACTIVE_WORKOUT_ID
                    } else id

                    workoutRepository.findWorkoutById(workoutId)?.let { workout ->
                        updateState {
                            it.copy(
                                workout = workout,
                                initialWorkout = workout
                            )
                        }

                        return@let workout
                    }.also { workout ->
                        if (mode.isExecute) {
                            startTimer(initialTime = workout?.duration ?: 0L)
                        }
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

    private fun fetchLatestSetsForExercises() = launchLoading {
        val sets = previousSetRepository.findPreviousSets().groupBy { it.exerciseId }

        Log.d("@@@", "Sets: ${sets.keys}")

        updateState {
            it.copy(
                previousSets = sets
            )
        }
    }

    private fun finishWorkout(userId: String, updateExistingWorkout: Boolean) = launchLoading {
        stopTimer()
        observeActiveWorkoutJob?.cancel()

        with(uiState.value) {
            val workout = workout.copy(
                unit = settings.unit,
                name = workout.name.ifBlank { "Workout" }
            )

            val workoutHistoryId = historyRepository.saveWorkoutHistory(
                userId = userId,
                workout = workout,
            )

            if (updateExistingWorkout && workoutId != null) {
                workoutRepository.saveWorkout(
                    userId = userId,
                    workout = workout.copyFromActiveWorkout(workoutId)
                )
            }

            triggerEvent(ManageWorkoutUiEvent.Navigate.History.Detail(workoutHistoryId))
        }
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

    private fun save(updateExistingWorkout: Boolean) {
        val userId = userRepository.getUser()?.id ?: return

        when (uiState.value.mode) {
            WorkoutMode.EXECUTE -> finishWorkout(userId, updateExistingWorkout)
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
        observeActiveWorkoutJob?.cancel()
        workoutRepository.deleteActiveWorkout()
        triggerEvent(ManageWorkoutUiEvent.Navigate.Back)
    }

    override fun onAction(action: ManageWorkoutUiAction) {
        when (action) {
            is ManageWorkoutUiAction.Workout -> when (action) {
                is ManageWorkoutUiAction.Workout.Save -> save(action.updateExistingWorkout)
                ManageWorkoutUiAction.Workout.Delete -> delete()
                ManageWorkoutUiAction.Workout.Discard -> discard()
                else -> workoutStateManager.onAction(action)
            }

            is ManageWorkoutUiAction.Exercise -> when (action) {
                ManageWorkoutUiAction.Exercise.OnReorderClicked -> triggerEvent(
                    ManageWorkoutUiEvent.Navigate.Exercise.Reorder(uiState.value.workout.exercises)
                )

                else -> exerciseStateManager.onAction(action)
            }

            is ManageWorkoutUiAction.Set -> setStateManager.onAction(action)
            is ManageWorkoutUiAction.Sheet -> sheetStateManager.onAction(action)
            is ManageWorkoutUiAction.Dialog -> dialogStateManager.onAction(action)
            is ManageWorkoutUiAction.NavResult -> navResultStateManager.onAction(action)

            is ManageWorkoutUiAction.Navigate.Exercise.Detail -> triggerEvent(
                ManageWorkoutUiEvent.Navigate.Exercise.Detail(action.id)
            )

            ManageWorkoutUiAction.Navigate.Workout.Edit -> triggerEvent(
                ManageWorkoutUiEvent.Navigate.Workout.Edit(id)
            )

            ManageWorkoutUiAction.Navigate.Workout.Execute -> triggerEvent(
                ManageWorkoutUiEvent.Navigate.Workout.Execute(id)
            )

            ManageWorkoutUiAction.Navigate.Back -> triggerEvent(
                ManageWorkoutUiEvent.Navigate.Back
            )
        }
    }
}