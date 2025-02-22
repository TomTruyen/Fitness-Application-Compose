package com.tomtruyen.feature.workouts.manage

sealed class ManageWorkoutUiEvent {
    data object NavigateToReplaceExercise: ManageWorkoutUiEvent()
    data object NavigateToAddExercise: ManageWorkoutUiEvent()
    data object NavigateBack: ManageWorkoutUiEvent()
    data class NavigateToDetail(val id: String): ManageWorkoutUiEvent()
    data class ScrollToExercise(val index: Int): ManageWorkoutUiEvent()
}
