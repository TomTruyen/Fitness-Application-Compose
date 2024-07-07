package com.tomtruyen.feature.exercises.detail

sealed class ExerciseDetailUiAction {
    data object Edit : ExerciseDetailUiAction()

    data object Delete : ExerciseDetailUiAction()
}
