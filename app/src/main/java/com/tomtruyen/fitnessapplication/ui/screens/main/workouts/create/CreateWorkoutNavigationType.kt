package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create

sealed class CreateWorkoutNavigationType {
    data object ReorderExercise : CreateWorkoutNavigationType()
    data object AddExercise: CreateWorkoutNavigationType()
}
