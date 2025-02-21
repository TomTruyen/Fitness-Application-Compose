package com.tomtruyen.feature.workouts.manage

sealed class ManageWorkoutUiEvent {
    data object NavigateToReplaceExercise: ManageWorkoutUiEvent()
    data object NavigateToAddExercise: ManageWorkoutUiEvent()

    data object NavigateBack: ManageWorkoutUiEvent()
}
