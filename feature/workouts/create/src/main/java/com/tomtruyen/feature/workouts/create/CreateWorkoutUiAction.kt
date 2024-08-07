package com.tomtruyen.feature.workouts.create

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse

sealed class CreateWorkoutUiAction {
    data class OnExerciseNotesChanged(val index: Int, val notes: String) : CreateWorkoutUiAction()

    data class OnDeleteExerciseClicked(val index: Int): CreateWorkoutUiAction()

    data class OnReorderExercises(val exercises: List<WorkoutExerciseResponse>) : CreateWorkoutUiAction()

    data object OnReorderExerciseClicked : CreateWorkoutUiAction()

    data object OnAddExerciseClicked : CreateWorkoutUiAction()

    data class OnAddExercise(val exercise: Exercise) : CreateWorkoutUiAction()

    data class OnRestEnabledChanged(val exerciseIndex: Int, val enabled: Boolean) : CreateWorkoutUiAction()

    data class OnRestChanged(val exerciseIndex: Int, val rest: Int) : CreateWorkoutUiAction()

    data class Save(val workoutName: String): CreateWorkoutUiAction()
}
