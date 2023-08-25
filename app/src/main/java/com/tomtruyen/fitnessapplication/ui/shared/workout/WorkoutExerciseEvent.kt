package com.tomtruyen.fitnessapplication.ui.shared.workout

import com.tomtruyen.fitnessapplication.ui.screens.main.workouts.create.CreateWorkoutUiEvent

sealed class WorkoutExerciseEvent(
    open val exerciseIndex: Int,
) {
    data class OnRepsChanged(
        override val exerciseIndex: Int,
        val setIndex: Int,
        val reps: String?
    ) : WorkoutExerciseEvent(exerciseIndex)
    data class OnWeightChanged(
        override val exerciseIndex: Int,
        val setIndex: Int,
        val weight: String?
    ) : WorkoutExerciseEvent(exerciseIndex)
    data class OnTimeChanged(
        override val exerciseIndex: Int,
        val setIndex: Int,
        val time: Int?
    ) : WorkoutExerciseEvent(exerciseIndex)
    data class OnDeleteSetClicked(
        override val exerciseIndex: Int,
        val setIndex: Int
    ) : WorkoutExerciseEvent(exerciseIndex)

    data class OnAddSetClicked(
        override val exerciseIndex: Int
    ) : WorkoutExerciseEvent(exerciseIndex)
}