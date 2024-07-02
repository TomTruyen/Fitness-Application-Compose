package com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.reorder

sealed class ReorderWorkoutExercisesUiAction {
    data class OnReorder(val from: Int, val to: Int): ReorderWorkoutExercisesUiAction()
    data object OnApplyReorder : ReorderWorkoutExercisesUiAction()
}
