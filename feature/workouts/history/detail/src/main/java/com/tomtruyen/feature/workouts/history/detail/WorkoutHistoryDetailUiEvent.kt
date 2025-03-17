package com.tomtruyen.feature.workouts.history.detail

import com.tomtruyen.core.common.models.WorkoutMode
import com.tomtruyen.data.models.ui.WorkoutUiModel

sealed class WorkoutHistoryDetailUiEvent {
    sealed class Navigate : WorkoutHistoryDetailUiEvent() {
        sealed class Exercise : Navigate() {
            data class Detail(val id: String) : Exercise()
        }

        data class Workout(val workout: WorkoutUiModel, val mode: WorkoutMode) : Navigate()

        data object Back : Navigate()
    }
}