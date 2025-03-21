package com.tomtruyen.navigation

import androidx.annotation.Keep
import com.tomtruyen.core.common.models.ExerciseMode
import com.tomtruyen.core.common.models.WorkoutMode
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import kotlinx.serialization.Serializable

@Keep
sealed interface Screen {
    sealed interface Auth : Screen {
        @Serializable
        data object Graph : Auth

        @Serializable
        data object Login : Auth

        @Serializable
        data object Register : Auth
    }

    @Serializable
    data object Settings : Screen

    sealed interface Exercise : Screen {
        @Serializable
        data object Graph : Exercise

        @Serializable
        data class Overview(val mode: ExerciseMode = ExerciseMode.VIEW) : Exercise

        @Serializable
        data class Detail(val id: String) : Exercise

        @Serializable
        data class Manage(val id: String? = null) : Exercise

        @Serializable
        data object Filter : Exercise
    }

    sealed interface Workout : Screen {
        @Serializable
        data object Graph : Workout

        @Serializable
        data object Overview : Workout

        @Serializable
        data class Manage(
            val id: String? = null,
            val mode: WorkoutMode,
            val workout: WorkoutUiModel = WorkoutUiModel()
        ) : Workout

        @Serializable
        data class Reorder(
            val exercises: List<WorkoutExerciseUiModel>
        )
    }

    sealed interface History : Screen {
        @Serializable
        data object Graph : History

        @Serializable
        data object Overview : History

        @Serializable
        data class Detail(val id: String) : History
    }
}