package com.tomtruyen.fitnessapplication.model

data class FetchedData(
    val data: MutableMap<Type, Boolean> = mutableMapOf()
) {
    fun hasFetched(type: Type): Boolean {
        return data[type] ?: false
    }

    fun setFetched(type: Type, fetched: Boolean) {
        data[type] = fetched
    }

    enum class Type {
        EXERCISES,
        USER_EXERCISES,
        SETTINGS,
        WORKOUTS
    }
}