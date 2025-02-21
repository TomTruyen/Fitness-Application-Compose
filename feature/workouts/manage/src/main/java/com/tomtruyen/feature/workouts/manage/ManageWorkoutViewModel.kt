package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.core.common.base.BaseViewModel
import com.tomtruyen.core.common.base.SnackbarMessage
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.WorkoutSet
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse
import com.tomtruyen.data.repositories.interfaces.SettingsRepository
import com.tomtruyen.data.repositories.interfaces.UserRepository
import com.tomtruyen.data.repositories.interfaces.WorkoutRepository
import com.tomtruyen.feature.workouts.manage.models.ManageWorkoutMode
import com.tomtruyen.feature.workouts.shared.WorkoutExerciseUiAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ManageWorkoutViewModel(
    private val id: String?,
    execute: Boolean,
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository,
    private val settingsRepository: SettingsRepository
): BaseViewModel<ManageWorkoutUiState, ManageWorkoutUiAction, ManageWorkoutUiEvent>(
    initialState = ManageWorkoutUiState(
        mode = ManageWorkoutMode.fromArgs(id, execute)
    )
) {
    init {
        findWorkout()

        observeLoading()
        observeSettings()
    }

    private fun findWorkout() = launchLoading(Dispatchers.IO) {
        if(uiState.value.mode == ManageWorkoutMode.CREATE || id == null) return@launchLoading

        workoutRepository.findWorkoutById(id)?.let {
            updateState { state ->
                state.copy(
                    initialWorkout = it.toWorkoutResponse(),
                    workout = it.toWorkoutResponse()
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

    private fun save() = vmScope.launch {
        val userId = userRepository.getUser()?.uid ?: return@launch

        isLoading(true)

        val workout = uiState.value.workout.apply {
            exercises.forEachIndexed { index, workoutExerciseResponse ->
                workoutExerciseResponse.order = index
            }
            unit = uiState.value.settings.unit

            if(name.isBlank()) {
                name = "Workout"
            }
        }

        workoutRepository.saveWorkout(
            userId = userId,
            workout = workout,
            isUpdate = uiState.value.mode == ManageWorkoutMode.EDIT,
            callback = object: FirebaseCallback<Unit> {
                override fun onSuccess(value: Unit) {
                    triggerEvent(ManageWorkoutUiEvent.NavigateBack)
                }

                override fun onError(error: String?) {
                    showSnackbar(SnackbarMessage.Error(error))
                }

                override fun onStopLoading() {
                    isLoading(false)
                }
            }
        )
    }

    private fun createWorkoutExercise(exercise: Exercise) = with(uiState.value) {
        WorkoutExerciseResponse(
            exercise = exercise,
            rest = settings.rest,
            restEnabled = settings.restEnabled,
        ).apply { sets = listOf(WorkoutSet(workoutExerciseId = this@apply.id)) }
    }

    private fun updateWorkoutName(name: String) = updateState {
        it.copy(
            workout = it.workout.copy(name = name)
        )
    }

    private fun updateExerciseNotes(id: String, notes: String) = updateState {
        it.copy(
            workout = it.workout.copy(
                exercises = it.workout.exercises.map { exercise ->
                    if (exercise.id == id) {
                        exercise.copy(notes = notes)
                    } else exercise
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

    private fun replaceExercise(exercise: Exercise) {
        var index = 0

        updateState {
            it.copy(
                workout = it.workout.copy(
                    exercises = it.workout.exercises.toMutableList().apply {
                        index = indexOfFirst { exercise -> exercise.id == it.selectedExerciseId }

                        if(index == -1) return@apply

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
    }

    private fun addExercises(exercises: List<Exercise>) = updateAndGetState {
        val newExercises = exercises.map(::createWorkoutExercise)

        it.copy(
            workout = it.workout.copy(
                exercises = it.workout.exercises + newExercises
            ),
        )
    }.also { state ->
        triggerEvent(ManageWorkoutUiEvent.ScrollToExercise(state.workout.exercises.size - 1))
    }

    private fun toggleExerciseMoreActionSheet(id: String? = null) = updateState {
        val shouldToggle = it.selectedExerciseId != id

        it.copy(
            selectedExerciseId = id,
            showExerciseMoreActions = if(shouldToggle) {
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
            showSetMoreActions = if(shouldToggle) {
                !it.showSetMoreActions
            } else {
                it.showSetMoreActions
            }
        )
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
        }
    }

    fun onWorkoutExerciseAction(event: WorkoutExerciseUiAction) {
        when (event) {
            is WorkoutExerciseUiAction.OnRepsChanged -> updateReps(
                id = event.id,
                setIndex = event.setIndex,
                reps = event.reps
            )

            is WorkoutExerciseUiAction.OnWeightChanged -> updateWeight(
                id = event.id,
                setIndex = event.setIndex,
                weight = event.weight
            )

            is WorkoutExerciseUiAction.OnTimeChanged -> updateTime(
                id = event.id,
                setIndex = event.setIndex,
                time = event.time
            )

            is WorkoutExerciseUiAction.OnDeleteSet -> {
                toggleSetMoreActionSheet()
                deleteSet(
                    id = event.id,
                    setIndex = event.setIndex
                )
            }

            is WorkoutExerciseUiAction.OnAddSet -> addSet(
                id = event.id
            )
        }
    }
}
