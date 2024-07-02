package com.tomtruyen.fitnessapplication.ui.screens.main.exercises.detail

sealed class ExerciseDetailUiAction {
    data object Edit : ExerciseDetailUiAction()
    data object Delete : ExerciseDetailUiAction()
}
