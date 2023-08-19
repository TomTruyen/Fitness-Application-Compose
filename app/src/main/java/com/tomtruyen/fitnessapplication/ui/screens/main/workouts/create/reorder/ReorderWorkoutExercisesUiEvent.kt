package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder

sealed class ReorderWorkoutExercisesUiEvent {
    data class OnReorder(val from: Int, val to: Int): ReorderWorkoutExercisesUiEvent()
    data object OnApplyReorder : ReorderWorkoutExercisesUiEvent()
}
