package com.tomtruyen.feature.exercises.manage

sealed class ManageExerciseUiEvent {
    sealed class Navigate: ManageExerciseUiEvent() {
        data object Back: Navigate()
    }
}
