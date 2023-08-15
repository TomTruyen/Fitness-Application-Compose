package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

sealed class ExerciseDetailNavigationType {
    data class Edit(val id: String): ExerciseDetailNavigationType()
    data object Back: ExerciseDetailNavigationType()
}
