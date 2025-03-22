package com.tomtruyen.feature.workouts.history.detail

import com.tomtruyen.core.common.models.actions.ExerciseActions

class WorkoutHistoryExerciseActions(
    private val onAction: (WorkoutHistoryDetailUiAction) -> Unit
): ExerciseActions {
    override fun navigateDetail(id: String) = onAction(
        WorkoutHistoryDetailUiAction.Navigate.Exercise.Detail(
            id = id,
        )
    )
}