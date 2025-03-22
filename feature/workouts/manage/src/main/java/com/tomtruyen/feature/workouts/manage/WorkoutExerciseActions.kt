package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.core.common.models.actions.ExerciseActions

class WorkoutExerciseActions(
    private val onAction: (ManageWorkoutUiAction) -> Unit
): ExerciseActions {
    override fun notesChanged(id: String, notes: String) = onAction(
        ManageWorkoutUiAction.Exercise.OnNotesChanged(
            id = id,
            notes = notes
        )
    )

    override fun navigateDetail(id: String) = onAction(
        ManageWorkoutUiAction.Navigate.Exercise.Detail(
            id = id
        )
    )

    override fun showSheet(id: String) = onAction(
        ManageWorkoutUiAction.Sheet.Exercise.Show(
            id = id
        )
    )
}