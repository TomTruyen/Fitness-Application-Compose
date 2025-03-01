package com.tomtruyen.feature.workouts.manage

import android.util.Log
import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.utils.StopwatchTimer
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutExerciseSet
import com.tomtruyen.data.entities.WorkoutExerciseWithSets
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.data.models.ui.copyWithAddSet
import com.tomtruyen.data.models.ui.copyWithDeleteSet
import com.tomtruyen.data.models.ui.copyWithRepsChanged
import com.tomtruyen.data.models.ui.copyWithSetCompleted
import com.tomtruyen.data.models.ui.copyWithTimeChanged
import com.tomtruyen.data.models.ui.copyWithWeightChanged
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutHistoryRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import com.tomtruyen.feature.workouts.manage.models.ManageWorkoutMode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.util.UUID

class ManageWorkoutViewModel(
    private val id: String?,
    execute: Boolean,
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository,
    private val historyRepository: WorkoutHistoryRepository,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ManageWorkoutUiState, ManageWorkoutUiAction, ManageWorkoutUiEvent>(
    initialState = ManageWorkoutUiState(
        mode = ManageWorkoutMode.fromArgs(id, execute)
    )
) {
    private val timer by lazy { StopwatchTimer() }

    init {
        findWorkout()

        observeLoading()
        observeSettings()

        startTimer()
    }

    private fun startTimer() {
        if (uiState.value.mode != ManageWorkoutMode.EXECUTE) return

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
        if (uiState.value.mode != ManageWorkoutMode.EXECUTE) return

        timer.stop()
    }

    private fun findWorkout() = launchLoading {
        if (uiState.value.mode == ManageWorkoutMode.CREATE || id == null) return@launchLoading

        workoutRepository.findWorkoutById(id)?.let { workout ->
            updateState {
                it.copy(
                    initialWorkout = workout,
                    workout = workout
                )
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

    private fun finishWorkout(userId: String) = launchLoading {
        stopTimer()

        // TODO: Setup the logic to actually save a workout to the History using `historyRepository`
    }

    private fun saveWorkout(userId: String) = launchLoading {
        with(uiState.value) {
            workoutRepository.saveWorkout(
                userId = userId,
                workout = workout.copy(
                    exercises = workout.exercises.mapIndexed { index, exercise ->
                        exercise.copy(
                            sortOrder = index,
                        )
                    },
                    unit = settings.unit,
                    name = workout.name.ifBlank { "Workout" }
                ),
            )

            triggerEvent(ManageWorkoutUiEvent.NavigateBack)
        }
    }


    private fun save() {
        val userId = userRepository.getUser()?.id ?: return

        when (uiState.value.mode) {
            ManageWorkoutMode.EXECUTE -> finishWorkout(userId)
            else -> saveWorkout(userId)
        }
    }

    private fun createWorkoutExercise(exercise: ExerciseUiModel): WorkoutExerciseUiModel {
        return WorkoutExerciseUiModel.createFromExerciseModel(
            model = exercise
        )
    }

    private fun updateWorkoutName(name: String) = updateState {
        it.copy(
            workout = it.workout.copy(
                name = name
            ),
        )
    }

    private fun updateExerciseNotes(id: String, notes: String) = updateState {
        it.copy(
            workout = it.workout.copy(
                exercises = it.workout.exercises.map { exercise ->
                    if(exercise.id == id) {
                        return@map exercise.copy(
                            notes = notes
                        )
                    }

                    exercise
                }
            )
        )
    }

    private fun reorderExercises(from: Int, to: Int) = updateState {
        it.copy(
            workout = it.workout.copy(
                exercises = it.workout.exercises.toMutableList().apply {
                    val exercise = removeAt(from)
                    add(to, exercise)
                }
            )
        )
    }

    private fun replaceExercise(exercise: ExerciseUiModel) {
        var index = 0

        updateState {
            it.copy(
                workout = it.workout.copy(
                    exercises = it.workout.exercises.toMutableList().apply {
                        index = indexOfFirst { exercise ->
                            exercise.id == it.selectedExerciseId
                        }

                        if (index == -1) return@apply

                        set(
                            index = index,
                            element = createWorkoutExercise(exercise)
                        )
                    }
                )
            )
        }

        triggerEvent(ManageWorkoutUiEvent.ScrollToExercise(index))
    }

    private fun deleteExercise() = updateState {
        it.copy(
            workout = it.workout.copy(
                exercises = it.workout.exercises.toMutableList().apply {
                    removeIf { exercise -> exercise.id == it.selectedExerciseId }
                }
            ),
            selectedExerciseId = null,
        )
    }.also { toggleExerciseMoreActionSheet(show = false) }

    private fun addExercises(exercises: List<ExerciseUiModel>) =
        updateAndGetState {
            val newExercises = exercises.map(::createWorkoutExercise)

            it.copy(
                workout = it.workout.copy(
                    exercises = it.workout.exercises + newExercises
                )
            )
        }.also { state ->
            triggerEvent(ManageWorkoutUiEvent.ScrollToExercise(state.workout.exercises.size - 1))
        }

    private fun setSelectedExerciseId(id: String?) = updateState {
        it.copy(selectedExerciseId = id)
    }

    private fun setSelectedSetIndex(index: Int?) = updateState {
        it.copy(selectedSetIndex = index)
    }

    private fun toggleExerciseMoreActionSheet(show: Boolean) = updateState {
        it.copy(showExerciseMoreActions = show)
    }

    private fun toggleSetMoreActionSheet(show: Boolean) = updateState {
        it.copy(showSetMoreActions = show)
    }

    private fun updateReps(id: String, setIndex: Int, reps: String?) = updateState {
        it.copy(
            workout = it.workout.copyWithRepsChanged(
                id = id,
                setIndex = setIndex,
                reps = reps
            )
        )
    }

    private fun updateWeight(id: String, setIndex: Int, weight: String?) = updateState {
        it.copy(
            workout = it.workout.copyWithWeightChanged(
                id = id,
                setIndex = setIndex,
                weight = weight
            )
        )
    }

    private fun updateTime(id: String, setIndex: Int, time: Int?) = updateState {
        it.copy(
            workout = it.workout.copyWithTimeChanged(
                id = id,
                setIndex = setIndex,
                time = time
            )
        )
    }

    private fun deleteSet(id: String, setIndex: Int) = updateState {
        it.copy(
            workout = it.workout.copyWithDeleteSet(
                id = id,
                setIndex = setIndex
            )
        )
    }

    private fun addSet(id: String) = updateState {
        it.copy(
            workout = it.workout.copyWithAddSet(
                id = id,
            )
        )
    }

    private fun toggleSetCompleted(id: String, setIndex: Int) = updateState {
        it.copy(
            workout = it.workout.copyWithSetCompleted(
                id = id,
                setIndex = setIndex
            )
        )
    }

    override fun onAction(action: ManageWorkoutUiAction) {
        when (action) {
            is ManageWorkoutUiAction.Save -> save()

            is ManageWorkoutUiAction.OnWorkoutNameChanged -> updateWorkoutName(
                name = action.name
            )

            is ManageWorkoutUiAction.OnExerciseNotesChanged -> updateExerciseNotes(
                id = action.id,
                notes = action.notes
            )

            is ManageWorkoutUiAction.OnDeleteExercise -> deleteExercise()

            is ManageWorkoutUiAction.OnReplaceExerciseClicked -> {
                toggleExerciseMoreActionSheet(show = false)
                triggerEvent(ManageWorkoutUiEvent.NavigateToReplaceExercise)
            }

            is ManageWorkoutUiAction.OnReplaceExercise -> replaceExercise(
                exercise = action.exercise
            )

            is ManageWorkoutUiAction.OnAddExerciseClicked -> triggerEvent(ManageWorkoutUiEvent.NavigateToAddExercise)

            is ManageWorkoutUiAction.OnAddExercises -> addExercises(
                exercises = action.exercises
            )

            is ManageWorkoutUiAction.OnReorder -> reorderExercises(
                from = action.from,
                to = action.to
            )

            is ManageWorkoutUiAction.ToggleExerciseMoreActionSheet -> {
                setSelectedExerciseId(action.id)
                toggleExerciseMoreActionSheet(!uiState.value.showExerciseMoreActions)
            }

            is ManageWorkoutUiAction.ToggleSetMoreActionSheet -> {
                setSelectedExerciseId(action.id)
                setSelectedSetIndex(action.setIndex)
                toggleSetMoreActionSheet(!uiState.value.showSetMoreActions)
            }

            is ManageWorkoutUiAction.OnRepsChanged -> updateReps(
                id = action.id,
                setIndex = action.setIndex,
                reps = action.reps
            )

            is ManageWorkoutUiAction.OnWeightChanged -> updateWeight(
                id = action.id,
                setIndex = action.setIndex,
                weight = action.weight
            )

            is ManageWorkoutUiAction.OnTimeChanged -> updateTime(
                id = action.id,
                setIndex = action.setIndex,
                time = action.time
            )

            is ManageWorkoutUiAction.OnDeleteSet -> {
                toggleSetMoreActionSheet(false)

                deleteSet(
                    id = action.id,
                    setIndex = action.setIndex
                )
            }

            is ManageWorkoutUiAction.OnAddSet -> addSet(
                id = action.id
            )

            is ManageWorkoutUiAction.ToggleSetCompleted -> toggleSetCompleted(
                id = action.id,
                setIndex = action.setIndex
            )
        }
    }
}