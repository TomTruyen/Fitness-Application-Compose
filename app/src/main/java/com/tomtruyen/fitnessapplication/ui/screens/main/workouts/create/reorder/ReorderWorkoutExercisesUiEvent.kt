package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder

import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse

sealed class ReorderWorkoutExercisesUiEvent {
    data class OnApplyReorder(val exercises: List<WorkoutExerciseResponse>) : ReorderWorkoutExercisesUiEvent()
}
