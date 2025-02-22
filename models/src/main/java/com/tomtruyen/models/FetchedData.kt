package com.tomtruyen.models

object DataFetchTracker {
    private val data: HashMap<String, Boolean> = hashMapOf()

    const val EXERCISES = "exercises"
    const val USER_EXERCISES = "user_exercises"
    const val SETTINGS = "settings"
    const val WORKOUTS = "workouts"
    const val LAST_WORKOUT = "last_workout"
    const val WORKOUT_HISTORY = "workout_history"

    fun isFetched(identifier: String) = data.getOrDefault(identifier, false)

    fun fetched(identifier: String) {
        data[identifier] = true
    }

    fun clear() = data.clear()
}