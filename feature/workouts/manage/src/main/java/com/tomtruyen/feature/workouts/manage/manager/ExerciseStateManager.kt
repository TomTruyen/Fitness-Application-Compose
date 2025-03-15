package com.tomtruyen.feature.workouts.manage.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiAction
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiEvent
import com.tomtruyen.feature.workouts.manage.ManageWorkoutUiState
import kotlinx.coroutines.Job

class ExerciseStateManager(
    private val updateState: ((ManageWorkoutUiState) -> ManageWorkoutUiState) -> Unit,
    private val updateStateAndGet: ((ManageWorkoutUiState) -> ManageWorkoutUiState) -> ManageWorkoutUiState,
    private val triggerEvent: (ManageWorkoutUiEvent) -> Job,
) : StateManager<ManageWorkoutUiAction.Exercise> {
    private fun updateExerciseNotes(id: String, notes: String) = updateState {
        it.copy(
            workout = it.workout.copy(
                exercises = it.workout.exercises.map { exercise ->
                    if (exercise.id == id) {
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

                        val setCount = this[index].sets.size

                        set(
                            index = index,
                            element = createWorkoutExercise(
                                exercise = exercise,
                                setCount = setCount
                            )
                        )
                    }
                )
            )
        }

        triggerEvent(ManageWorkoutUiEvent.ScrollToExercise(index))
    }

    private fun addExercises(exercises: List<ExerciseUiModel>) = updateStateAndGet {
        val newExercises = exercises.map(::createWorkoutExercise)

        it.copy(
            workout = it.workout.copy(
                exercises = it.workout.exercises + newExercises
            )
        )
    }.also { state -> triggerEvent(ManageWorkoutUiEvent.ScrollToExercise(state.workout.exercises.size - 1)) }

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

    private fun createWorkoutExercise(exercise: ExerciseUiModel, setCount: Int = 1): WorkoutExerciseUiModel {
        return WorkoutExerciseUiModel.createFromExerciseModel(
            model = exercise,
            setCount = setCount
        )
    }

    override fun onAction(action: ManageWorkoutUiAction.Exercise) {
        when (action) {
            is ManageWorkoutUiAction.Exercise.OnNotesChanged -> updateExerciseNotes(
                id = action.id,
                notes = action.notes
            )

            is ManageWorkoutUiAction.Exercise.Reorder -> reorderExercises(
                from = action.from,
                to = action.to
            )

            ManageWorkoutUiAction.Exercise.OnAddClicked -> triggerEvent(
                ManageWorkoutUiEvent.Navigate.Exercise.Add
            )

            is ManageWorkoutUiAction.Exercise.Add -> addExercises(
                exercises = action.exercises
            )

            is ManageWorkoutUiAction.Exercise.Replace -> replaceExercise(
                exercise = action.exercise
            )

            ManageWorkoutUiAction.Exercise.OnReplaceClicked -> triggerEvent(
                ManageWorkoutUiEvent.Navigate.Exercise.Replace
            )

            ManageWorkoutUiAction.Exercise.Delete -> deleteExercise()
        }
    }
}