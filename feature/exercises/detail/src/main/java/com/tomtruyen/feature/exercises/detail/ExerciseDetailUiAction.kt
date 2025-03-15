package com.tomtruyen.feature.exercises.detail

sealed class ExerciseDetailUiAction {
    data object Edit : ExerciseDetailUiAction()

    data object Delete : ExerciseDetailUiAction()

    sealed class Sheet: ExerciseDetailUiAction() {
        data object Show: Sheet()

        data object Dismiss: Sheet()
    }

    sealed class Dialog: ExerciseDetailUiAction() {
        data object Show: Dialog()

        data object Dismiss: Dialog()
    }
}
