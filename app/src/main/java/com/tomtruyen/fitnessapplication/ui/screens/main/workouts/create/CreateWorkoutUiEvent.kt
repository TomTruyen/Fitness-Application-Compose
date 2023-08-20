package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.networking.WorkoutExerciseResponse

sealed class CreateWorkoutUiEvent {
    data class OnSettingsChanged(val settings: Settings?) : CreateWorkoutUiEvent()
    data class OnExerciseNotesChanged(val index: Int, val notes: String) : CreateWorkoutUiEvent()
    data class OnDeleteExerciseClicked(val index: Int): CreateWorkoutUiEvent()
    data class OnReorderExercises(val exercises: List<WorkoutExerciseResponse>) : CreateWorkoutUiEvent()
    data object OnReorderExerciseClicked : CreateWorkoutUiEvent()
    data object OnAddExerciseClicked : CreateWorkoutUiEvent()
    data class OnRepsChanged(val exerciseIndex: Int, val setIndex: Int, val reps: String?) : CreateWorkoutUiEvent()
    data class OnWeightChanged(val exerciseIndex: Int, val setIndex: Int, val weight: String?) : CreateWorkoutUiEvent()
    data class OnDeleteSetClicked(val exerciseIndex: Int, val setIndex: Int) : CreateWorkoutUiEvent()
    data class OnAddSetClicked(val exerciseIndex: Int) : CreateWorkoutUiEvent()
    data class Save(val workoutName: String): CreateWorkoutUiEvent()
}
