package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.entities.WorkoutWithExercises
import com.tomtruyen.feature.workouts.manage.models.ManageWorkoutMode

data class ManageWorkoutUiState(
    val mode: ManageWorkoutMode = ManageWorkoutMode.CREATE,
    val initialWorkout: WorkoutWithExercises = WorkoutWithExercises(),
    val fullWorkout: WorkoutWithExercises = WorkoutWithExercises(),
    val settings: Settings = Settings(),
    val duration: Long = 0L,
    val loading: Boolean = false,

    // BottomSheet states
    val showExerciseMoreActions: Boolean = false,
    val showSetMoreActions: Boolean = false,
    val selectedExerciseId: String? = null,
    val selectedSetIndex: Int? = null
) {
    val workout = fullWorkout.workout
}

