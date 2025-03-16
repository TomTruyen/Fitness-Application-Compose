package com.tomtruyen.feature.workouts.manage

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.WorkoutMode
import com.tomtruyen.data.entities.PreviousSet
import com.tomtruyen.data.models.ui.SettingsUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel

@Immutable
data class ManageWorkoutUiState(
    // Used for Execute without ID to help us determine if we should allow the editing of workout.name
    val workoutId: String?,

    val mode: WorkoutMode = WorkoutMode.CREATE,
    val initialWorkout: WorkoutUiModel = WorkoutUiModel(),
    val workout: WorkoutUiModel = WorkoutUiModel(),
    val settings: SettingsUiModel = SettingsUiModel(),

    val previousSets: Map<String, List<PreviousSet>> = emptyMap(),

    val loading: Boolean = false,

    // BottomSheet states
    val showSaveSheet: Boolean = false,
    val showExerciseMoreActions: Boolean = false,
    val showSetMoreActions: Boolean = false,
    val showWorkoutMoreActions: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val showFinishConfirmation: Boolean = false,
    val selectedExerciseId: String? = null,
    val selectedSetIndex: Int? = null
) {
    val shouldShowFinishConfirmation: Boolean
        get() = mode.isExecute
                && workout.exercises.any { exercise ->
                    exercise.sets.any { !it.completed }
                }

    val shouldShowSaveSheet: Boolean
        get() = mode.isExecute
                    && workoutId != null
                    && !initialWorkout.isOriginalWorkout(workout)
}

