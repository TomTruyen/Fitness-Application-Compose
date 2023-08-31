package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder

import com.tomtruyen.fitnessapplication.networking.models.WorkoutExerciseResponse

sealed class ReorderWorkoutExercisesNavigationType {
    data class OnApplyReorder(val exercises: List<WorkoutExerciseResponse>) : ReorderWorkoutExercisesNavigationType()
}
