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
import com.tomtruyen.feature.workouts.shared.WorkoutExerciseEvent
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

    private fun updateExericseNotes(id: String, notes: String) = updateState {
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
        updateState {
            it.copy(
                workout = it.workout.copy(
                    exercises = it.workout.exercises.toMutableList().apply {
                        val index = indexOfFirst { exercise -> exercise.id == it.selectedExerciseId }
                        set(
                            index = index,
                            element = createWorkoutExercise(exercise)
                        )
                    }
                )
            )
        }
    }

    private fun deleteExercise() {
        updateState {
            it.copy(
                workout = it.workout.copy(
                    exercises = it.workout.exercises.toMutableList().apply {
                        removeIf { exercise -> exercise.id == it.selectedExerciseId }
                    }
                ),
                selectedExerciseId = null,
            )
        }
    }

    private fun addExercises(exercises: List<Exercise>) {
        updateState {
            val newExercises = exercises.map(::createWorkoutExercise)

            it.copy(
                workout = it.workout.copy(
                    exercises = it.workout.exercises + newExercises
                ),
            )
        }
    }

    private fun toggleMoreActionSheet(exerciseId: String? = null) = updateState {
        it.copy(
            selectedExerciseId = exerciseId,
            isMoreActionsSheetVisible = !it.isMoreActionsSheetVisible
        )
    }

    private fun closeMoreActionSheet() = updateState {
        it.copy(
            isMoreActionsSheetVisible = false
        )
    }

    override fun onAction(action: ManageWorkoutUiAction) {
        when (action) {
            is ManageWorkoutUiAction.Save -> save()
            is ManageWorkoutUiAction.OnWorkoutNameChanged -> updateWorkoutName(action.name)
            is ManageWorkoutUiAction.OnExerciseNotesChanged -> updateExericseNotes(action.id, action.notes)
            is ManageWorkoutUiAction.OnDeleteExercise -> {
                closeMoreActionSheet()
                deleteExercise()
            }
            is ManageWorkoutUiAction.OnReplaceExerciseClicked -> {
                closeMoreActionSheet()
                triggerEvent(ManageWorkoutUiEvent.NavigateToReplaceExercise)
            }
            is ManageWorkoutUiAction.OnReplaceExercise -> replaceExercise(action.exercise)
            is ManageWorkoutUiAction.OnAddExerciseClicked -> triggerEvent(ManageWorkoutUiEvent.NavigateToAddExercise)
            is ManageWorkoutUiAction.OnAddExercises -> addExercises(action.exercises)
            is ManageWorkoutUiAction.OnReorder -> reorderExercises(action.from, action.to)
            is ManageWorkoutUiAction.ShowMoreActionSheet -> toggleMoreActionSheet(action.id)
            is ManageWorkoutUiAction.DismissMoreActionsSheet -> toggleMoreActionSheet()
        }
    }

    fun onWorkoutEvent(event: WorkoutExerciseEvent) {
        when (event) {
            is WorkoutExerciseEvent.OnRepsChanged -> updateState {
                it.copy(
                    workout = it.workout.copyWithRepsChanged(
                        id = event.id,
                        setIndex = event.setIndex,
                        reps = event.reps
                    )
                )
            }

            is WorkoutExerciseEvent.OnWeightChanged -> updateState {
                it.copy(
                    workout = it.workout.copyWithWeightChanged(
                        id = event.id,
                        setIndex = event.setIndex,
                        weight = event.weight
                    )
                )
            }

            is WorkoutExerciseEvent.OnTimeChanged -> updateState {
                it.copy(
                    workout = it.workout.copyWithTimeChanged(
                        id = event.id,
                        setIndex = event.setIndex,
                        time = event.time
                    )
                )
            }

            is WorkoutExerciseEvent.OnDeleteSet -> updateState {
                it.copy(
                    workout = it.workout.copyWithDeleteSet(
                        id = event.id,
                        setIndex = event.setIndex
                    )
                )
            }

            is WorkoutExerciseEvent.OnAddSet -> updateState {
                it.copy(
                    workout = it.workout.copyWithAddSet(
                        id = event.id,
                    )
                )
            }
        }
    }
}
