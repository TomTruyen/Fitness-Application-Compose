package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.utils.StopwatchTimer
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.entities.WorkoutExercise
import com.tomtruyen.data.entities.WorkoutExerciseSet
import com.tomtruyen.data.entities.WorkoutExerciseWithSets
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
                    fullWorkout = workout
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
        with(uiState.value) {
            stopTimer()

            // TODO: Setup the logic to actually save a workout to the History using `historyRepository`
        }
    }

    private fun saveWorkout(userId: String) = launchLoading {
        with(uiState.value) {
            workoutRepository.saveWorkout(
                userId = userId,
                workout = fullWorkout.copy(
                    exercises = fullWorkout.exercises.mapIndexed { index, exercise ->
                        exercise.copy(
                            workoutExercise = exercise.workoutExercise.copy(
                                sortOrder = index
                            )
                        )
                    },
                    workout = fullWorkout.workout.copy(
                        unit = settings.unit,
                        name = if (fullWorkout.workout.name.isBlank()) "Workout" else fullWorkout.workout.name
                    )
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

    private fun createWorkoutExercise(exercise: ExerciseWithCategoryAndEquipment): WorkoutExerciseWithSets {
        val uuid = UUID.randomUUID().toString()

        return WorkoutExerciseWithSets(
            workoutExercise = WorkoutExercise(
                id = uuid
            ),
            exercise = exercise,
            sets = listOf(WorkoutExerciseSet(workoutExerciseId = uuid))
        )
    }

    private fun updateWorkoutName(name: String) = updateState {
        it.copy(
            fullWorkout = it.fullWorkout.copy(
                workout = it.fullWorkout.workout.copy(
                    name = name
                )
            ),
        )
    }

    private fun updateExerciseNotes(id: String, notes: String) = updateState {
        it.copy(
            fullWorkout = it.fullWorkout.copy(
                exercises = it.fullWorkout.exercises.map { exercise ->
                    if (exercise.workoutExercise.id == id) {
                        return@map exercise.copy(
                            workoutExercise = exercise.workoutExercise.copy(
                                notes = notes
                            )
                        )
                    }

                    exercise
                }
            )
        )
    }

    private fun reorderExercises(from: Int, to: Int) = updateState {
        it.copy(
            fullWorkout = it.fullWorkout.copy(
                exercises = it.fullWorkout.exercises.toMutableList().apply {
                    val exercise = removeAt(from)
                    add(to, exercise)
                }
            )
        )
    }

    private fun replaceExercise(exercise: ExerciseWithCategoryAndEquipment) {
        var index = 0

        updateState {
            it.copy(
                fullWorkout = it.fullWorkout.copy(
                    exercises = it.fullWorkout.exercises.toMutableList().apply {
                        index =
                            indexOfFirst { exercise -> exercise.workoutExercise.exerciseId == it.selectedExerciseId }

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
            fullWorkout = it.fullWorkout.copy(
                exercises = it.fullWorkout.exercises.toMutableList().apply {
                    removeIf { exercise -> exercise.workoutExercise.id == it.selectedExerciseId }
                }
            ),
            selectedExerciseId = null,
        )
    }

    private fun addExercises(exercises: List<ExerciseWithCategoryAndEquipment>) =
        updateAndGetState {
            val newExercises = exercises.map(::createWorkoutExercise)

            it.copy(
                fullWorkout = it.fullWorkout.copy(
                    exercises = it.fullWorkout.exercises + newExercises
                )
            )
        }.also { state ->
            triggerEvent(ManageWorkoutUiEvent.ScrollToExercise(state.fullWorkout.exercises.size - 1))
        }

    private fun toggleExerciseMoreActionSheet(id: String? = null) = updateState {
        val shouldToggle = it.selectedExerciseId != id

        it.copy(
            selectedExerciseId = id,
            showExerciseMoreActions = if (shouldToggle) {
                !it.showExerciseMoreActions
            } else {
                it.showExerciseMoreActions
            }
        )
    }

    private fun toggleSetMoreActionSheet(id: String? = null, setIndex: Int? = null) = updateState {
        val shouldToggle = it.selectedSetIndex != setIndex || it.selectedExerciseId != id

        it.copy(
            selectedExerciseId = id,
            selectedSetIndex = setIndex,
            showSetMoreActions = if (shouldToggle) {
                !it.showSetMoreActions
            } else {
                it.showSetMoreActions
            }
        )
    }

    private fun updateReps(id: String, setIndex: Int, reps: String?) = updateState {
        it.copy(
            fullWorkout = it.fullWorkout.copyWithRepsChanged(
                id = id,
                setIndex = setIndex,
                reps = reps
            )
        )
    }

    private fun updateWeight(id: String, setIndex: Int, weight: String?) = updateState {
        it.copy(
            fullWorkout = it.fullWorkout.copyWithWeightChanged(
                id = id,
                setIndex = setIndex,
                weight = weight
            )
        )
    }

    private fun updateTime(id: String, setIndex: Int, time: Int?) = updateState {
        it.copy(
            fullWorkout = it.fullWorkout.copyWithTimeChanged(
                id = id,
                setIndex = setIndex,
                time = time
            )
        )
    }

    private fun deleteSet(id: String, setIndex: Int) = updateState {
        it.copy(
            fullWorkout = it.fullWorkout.copyWithDeleteSet(
                id = id,
                setIndex = setIndex
            )
        )
    }

    private fun addSet(id: String) = updateState {
        it.copy(
            fullWorkout = it.fullWorkout.copyWithAddSet(
                id = id,
            )
        )
    }

    private fun toggleSetCompleted(id: String, setIndex: Int) = updateState {
        it.copy(
            fullWorkout = it.fullWorkout.copyWithSetCompleted(
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

            is ManageWorkoutUiAction.OnDeleteExercise -> {
                toggleExerciseMoreActionSheet(uiState.value.selectedExerciseId)
                deleteExercise()
            }

            is ManageWorkoutUiAction.OnReplaceExerciseClicked -> {
                toggleExerciseMoreActionSheet(uiState.value.selectedExerciseId)
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

            is ManageWorkoutUiAction.ToggleExerciseMoreActionSheet -> toggleExerciseMoreActionSheet(
                id = action.id
            )

            is ManageWorkoutUiAction.ToggleSetMoreActionSheet -> toggleSetMoreActionSheet(
                id = action.id,
                setIndex = action.setIndex
            )

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
                toggleSetMoreActionSheet()
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