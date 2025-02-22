package com.tomtruyen.feature.exercises.manage

sealed class ManageExerciseUiEvent {
    data object NavigateBack : ManageExerciseUiEvent()
}
