package com.tomtruyen.fitnessapplication.model

data class FetchedData(
    val data: MutableMap<String, Boolean> = mutableMapOf()
) {
    fun hasFetched(identifier: String): Boolean {
        return data[identifier] ?: false
    }

    fun setFetched(identifier: String, fetched: Boolean) {
        data[identifier] = fetched
    }

    enum class Type(val identifier: String) {
        EXERCISES("exercises"),
        USER_EXERCISES("user_exercises"),
        SETTINGS("settings"),
        WORKOUTS("workouts");
    }
}