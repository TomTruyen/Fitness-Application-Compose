package com.tomtruyen.feature.workouts.manage

sealed class ManageWorkoutUiEvent {
    data object NavigateToReplaceExercise : ManageWorkoutUiEvent()
    data object NavigateToAddExercise : ManageWorkoutUiEvent()
    data object NavigateBack : ManageWorkoutUiEvent()
    data class NavigateToHistoryDetail(val workoutHistoryId: String): ManageWorkoutUiEvent()
    data class ScrollToExercise(val index: Int) : ManageWorkoutUiEvent()
    data class NavigateToExerciseDetail(val id: String): ManageWorkoutUiEvent()
    data class NavigateToEditWorkout(val id: String?): ManageWorkoutUiEvent()
    data class NavigateToExecuteWorkout(val id: String?): ManageWorkoutUiEvent()
}
