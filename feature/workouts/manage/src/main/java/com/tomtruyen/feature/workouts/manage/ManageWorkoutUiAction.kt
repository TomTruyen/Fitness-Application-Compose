package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.models.WorkoutExerciseResponse

sealed class ManageWorkoutUiAction {
    data class OnWorkoutNameChanged(val name: String) : ManageWorkoutUiAction()

    data class OnExerciseNotesChanged(val id: String, val notes: String) : ManageWorkoutUiAction()

    data class OnDeleteExerciseClicked(val index: Int): ManageWorkoutUiAction()

    data class OnReorderExercises(val exercises: List<WorkoutExerciseResponse>) : ManageWorkoutUiAction()

    data object OnAddExerciseClicked : ManageWorkoutUiAction()

    data class OnAddExercises(val exercises: List<Exercise>) : ManageWorkoutUiAction()

    data class OnRestEnabledChanged(val exerciseIndex: Int, val enabled: Boolean) : ManageWorkoutUiAction()

    data class OnRestChanged(val exerciseIndex: Int, val rest: Int) : ManageWorkoutUiAction()

    data object Save: ManageWorkoutUiAction()

    data class OnReorder(val from: Int, val to: Int): ManageWorkoutUiAction()
}
