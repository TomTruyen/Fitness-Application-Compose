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
        data class Overview(val mode: Mode = Mode.VIEW) : Exercise {
            enum class Mode {
                VIEW,
                SELECT,
                REPLACE
            }
        }

        @Serializable
        data class Detail(val id: String): Exercise

        @Serializable
        data class Manage(val id: String? = null): Exercise

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
        data class Manage(val id: String? = null, val execute: Boolean = false): Workout

        @Serializable
        data object HistoryOverview: Workout
    }
}