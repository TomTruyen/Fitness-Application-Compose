package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.data.entities.Exercise

sealed class ManageWorkoutUiAction {
    data class OnWorkoutNameChanged(val name: String) : ManageWorkoutUiAction()

    data class OnExerciseNotesChanged(val id: String, val notes: String) : ManageWorkoutUiAction()

    data object OnDeleteExercise: ManageWorkoutUiAction()

    data object OnReplaceExerciseClicked: ManageWorkoutUiAction()

    data object OnAddExerciseClicked : ManageWorkoutUiAction()

    data class OnReplaceExercise(val exercise: Exercise): ManageWorkoutUiAction()

    data class OnAddExercises(val exercises: List<Exercise>) : ManageWorkoutUiAction()

    data object Save: ManageWorkoutUiAction()

    data class OnReorder(val from: Int, val to: Int): ManageWorkoutUiAction()

    data class ShowMoreActionSheet(val id: String): ManageWorkoutUiAction()

    data object DismissMoreActionsSheet: ManageWorkoutUiAction()
}
