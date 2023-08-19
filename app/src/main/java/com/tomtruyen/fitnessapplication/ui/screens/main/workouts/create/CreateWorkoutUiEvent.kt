package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

import com.tomtruyen.fitnessapplication.networking.WorkoutExerciseResponse

sealed class CreateWorkoutUiEvent {
    data class OnReorderExercises(val exercises: List<WorkoutExerciseResponse>) : CreateWorkoutUiEvent()
    data object OnReorderExerciseClicked : CreateWorkoutUiEvent()
    data object OnAddExerciseClicked : CreateWorkoutUiEvent()
}
