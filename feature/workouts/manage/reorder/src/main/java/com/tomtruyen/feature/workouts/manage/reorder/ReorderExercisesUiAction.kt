package com.tomtruyen.feature.workouts.manage.reorder

sealed class ReorderExercisesUiAction {
    data class Reorder(val from: Int, val to: Int) : ReorderExercisesUiAction()

    data object Submit : ReorderExercisesUiAction()
}
