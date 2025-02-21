package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.firebase.models.WorkoutResponse

data class ManageWorkoutUiState(
    val isEditing: Boolean = false,
    val initialWorkout: WorkoutResponse = WorkoutResponse(),
    val workout: WorkoutResponse = WorkoutResponse(),
    val settings: Settings = Settings(),

    val loading: Boolean = false,
)

