package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.firebase.models.WorkoutResponse
import com.tomtruyen.feature.workouts.manage.models.ManageWorkoutMode

data class ManageWorkoutUiState(
    val mode: ManageWorkoutMode = ManageWorkoutMode.CREATE,
    val initialWorkout: WorkoutResponse = WorkoutResponse(),
    val workout: WorkoutResponse = WorkoutResponse(),
    val settings: Settings = Settings(),
    val loading: Boolean = false,

    val isMoreActionsSheetVisible: Boolean = false,
    // Used for when we for example click on the more actions button of an exercise
    val selectedExerciseId: String? = null
)

