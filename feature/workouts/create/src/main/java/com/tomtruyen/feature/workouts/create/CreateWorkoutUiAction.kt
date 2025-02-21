package com.tomtruyen.feature.workouts.create

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse

sealed class CreateWorkoutUiAction {
    data class OnWorkoutNameChanged(val name: String) : CreateWorkoutUiAction()

    data class OnExerciseNotesChanged(val id: String, val notes: String) : CreateWorkoutUiAction()

    data class OnDeleteExerciseClicked(val index: Int): CreateWorkoutUiAction()

    data class OnReorderExercises(val exercises: List<WorkoutExerciseResponse>) : CreateWorkoutUiAction()

    data object OnAddExerciseClicked : CreateWorkoutUiAction()

    data class OnAddExercises(val exercises: List<Exercise>) : CreateWorkoutUiAction()

    data class OnRestEnabledChanged(val exerciseIndex: Int, val enabled: Boolean) : CreateWorkoutUiAction()

    data class OnRestChanged(val exerciseIndex: Int, val rest: Int) : CreateWorkoutUiAction()

    data object Save: CreateWorkoutUiAction()

    data class OnReorder(val from: Int, val to: Int): CreateWorkoutUiAction()
}
