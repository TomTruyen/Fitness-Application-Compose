package com.tomtruyen.feature.workouts.manage

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.models.ui.SettingsUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import com.tomtruyen.core.common.models.ManageWorkoutMode

@Immutable
data class ManageWorkoutUiState(
    val mode: ManageWorkoutMode = ManageWorkoutMode.CREATE,
    val initialWorkout: WorkoutUiModel = WorkoutUiModel(),
    val workout: WorkoutUiModel = WorkoutUiModel(),
    val settings: SettingsUiModel = SettingsUiModel(),
    val duration: Long = 0L,
    val loading: Boolean = false,

    // BottomSheet states
    val showExerciseMoreActions: Boolean = false,
    val showSetMoreActions: Boolean = false,
    val selectedExerciseId: String? = null,
    val selectedSetIndex: Int? = null
)

