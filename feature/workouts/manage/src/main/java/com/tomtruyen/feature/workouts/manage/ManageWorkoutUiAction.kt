package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.models.ui.ExerciseUiModel

sealed class ManageWorkoutUiAction {
    data class OnWorkoutNameChanged(val name: String) : ManageWorkoutUiAction()

    data class OnExerciseNotesChanged(val id: String, val notes: String) : ManageWorkoutUiAction()

    data object OnReplaceExerciseClicked : ManageWorkoutUiAction()

    data class OnReplaceExercise(val exercise: ExerciseUiModel) :
        ManageWorkoutUiAction()

    data object OnAddExerciseClicked : ManageWorkoutUiAction()

    data class OnAddExercises(val exercises: List<ExerciseUiModel>) :
        ManageWorkoutUiAction()

    data object OnDeleteExercise : ManageWorkoutUiAction()

    data class OnReorder(val from: Int, val to: Int) : ManageWorkoutUiAction()

    data class ToggleExerciseMoreActionSheet(val id: String? = null) : ManageWorkoutUiAction()

    data class ToggleSetMoreActionSheet(val id: String? = null, val setIndex: Int? = null) :
        ManageWorkoutUiAction()

    data object Save : ManageWorkoutUiAction()

    data class OnRepsChanged(
        val id: String,
        val setIndex: Int,
        val reps: String?
    ) : ManageWorkoutUiAction()

    data class OnWeightChanged(
        val id: String,
        val setIndex: Int,
        val weight: String?
    ) : ManageWorkoutUiAction()

    data class OnTimeChanged(
        val id: String,
        val setIndex: Int,
        val time: Int?
    ) : ManageWorkoutUiAction()

    data class OnDeleteSet(
        val id: String,
        val setIndex: Int
    ) : ManageWorkoutUiAction()

    data class OnAddSet(
        val id: String
    ) : ManageWorkoutUiAction()

    data class ToggleSetCompleted(
        val id: String,
        val setIndex: Int
    ) : ManageWorkoutUiAction()
}
