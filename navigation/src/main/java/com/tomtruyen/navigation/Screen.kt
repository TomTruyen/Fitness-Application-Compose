package com.tomtruyen.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    sealed interface Auth: Screen {
        @Serializable
        data object Graph: Auth

        @Serializable
        data object Login : Auth

        @Serializable
        data object Register: Auth
    }

    @Serializable
    data object Profile: Screen

    sealed interface Exercise: Screen {
        @Serializable
        data object Graph: Exercise

        @Serializable
        data class Overview(val isFromWorkout: Boolean = false) : Exercise

        @Serializable
        data class Detail(val id: String): Exercise

        @Serializable
        data class Create(val id: String? = null): Exercise

        @Serializable
        data object Filter: Exercise
    }

    sealed interface Workout: Screen {
        @Serializable
        data object Graph: Workout

        @Serializable
        data object Overview: Workout

        @Serializable
        data class Detail(val id: String): Workout

        @Serializable
        data class Create(val id: String? = null): Workout

        @Serializable
        data class Execute(val id: String): Workout

        @Serializable
        data object HistoryOverview: Workout

        @Serializable
        data object ReorderExercises: Workout
    }
}