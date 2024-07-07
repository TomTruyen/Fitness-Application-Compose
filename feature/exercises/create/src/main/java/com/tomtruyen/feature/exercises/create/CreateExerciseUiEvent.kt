package com.tomtruyen.feature.exercises.create

sealed class CreateExerciseUiEvent {
    data object NavigateBack : CreateExerciseUiEvent()
}
