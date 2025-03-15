package com.tomtruyen.feature.workouts.manage

import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel

sealed class ManageWorkoutUiEvent {
    sealed class Navigate : ManageWorkoutUiEvent() {
        sealed class Workout : Navigate() {
            data class Edit(val id: String?) : ManageWorkoutUiEvent()

            data class Execute(val id: String?) : ManageWorkoutUiEvent()
        }

        sealed class Exercise : Navigate() {
            data object Add : Exercise()

            data class Reorder(val exercises: List<WorkoutExerciseUiModel>): Exercise()

            data object Replace : Exercise()

            data class Detail(val id: String) : Exercise()
        }

        sealed class History : Navigate() {
            data class Detail(val workoutHistoryId: String) : History()
        }

        data object Back : Navigate()
    }

    data class ScrollToExercise(val index: Int) : ManageWorkoutUiEvent()
}
