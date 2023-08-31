package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.Settings
import com.tomtruyen.fitnessapplication.networking.models.WorkoutExerciseResponse

sealed class CreateWorkoutUiEvent {
    data class OnSettingsChanged(val settings: Settings?) : CreateWorkoutUiEvent()
    data class OnExerciseNotesChanged(val index: Int, val notes: String) : CreateWorkoutUiEvent()
    data class OnDeleteExerciseClicked(val index: Int): CreateWorkoutUiEvent()
    data class OnReorderExercises(val exercises: List<WorkoutExerciseResponse>) : CreateWorkoutUiEvent()
    data object OnReorderExerciseClicked : CreateWorkoutUiEvent()
    data object OnAddExerciseClicked : CreateWorkoutUiEvent()
    data class OnAddExercise(val exercise: Exercise) : CreateWorkoutUiEvent()
    data class OnRestEnabledChanged(val exerciseIndex: Int, val enabled: Boolean) : CreateWorkoutUiEvent()
    data class OnRestChanged(val exerciseIndex: Int, val rest: Int) : CreateWorkoutUiEvent()
    data class Save(val workoutName: String): CreateWorkoutUiEvent()
}
