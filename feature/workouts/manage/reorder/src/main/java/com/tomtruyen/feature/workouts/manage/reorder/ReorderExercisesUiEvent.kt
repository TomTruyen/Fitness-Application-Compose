package com.tomtruyen.feature.workouts.manage.reorder

import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel

sealed class ReorderExercisesUiEvent {
    sealed class Navigate : ReorderExercisesUiEvent() {
        data class Submit(val exercises: List<WorkoutExerciseUiModel>) : Navigate()
    }
}
