package com.tomtruyen.navigation

const val KEY_WORKOUT = "Workout"
const val KEY_EXERCISE = "Exercise"
const val KEY_HISTORY = "History"

sealed class SharedTransitionKey {
    abstract val key: String

    data class Workout(val id: String): SharedTransitionKey() {
        override val key: String
            get() = "${KEY_WORKOUT}_${id}"
    }

    data class Exercise(val id: String): SharedTransitionKey() {
        override val key: String
            get() = "${KEY_EXERCISE}_${id}"

        companion object {
            const val KEY_WORKOUT_ADD_EXERCISE = "Workout_Add_Exercise"
        }
    }

    data class History(val id: String): SharedTransitionKey() {
        override val key: String
            get() = "${KEY_HISTORY}_${id}"
    }
}