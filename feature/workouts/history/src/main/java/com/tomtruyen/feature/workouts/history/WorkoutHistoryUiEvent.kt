package com.tomtruyen.feature.workouts.history

import com.tomtruyen.core.common.models.WorkoutMode
import com.tomtruyen.data.models.ui.WorkoutUiModel

sealed class WorkoutHistoryUiEvent {
    sealed class Navigate : WorkoutHistoryUiEvent() {
        data class Detail(val id: String) : Navigate()

        data class Workout(val workout: WorkoutUiModel, val mode: WorkoutMode) : Navigate()
    }
}
