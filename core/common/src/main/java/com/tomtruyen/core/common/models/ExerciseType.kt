package com.tomtruyen.core.common.models

enum class ExerciseType(val value: String) {
    WEIGHT("Weight"),
    TIME("Time");

    companion object {
        fun fromValue(value: String) = entries.firstOrNull {
            it.value == value
        } ?: WEIGHT
    }
}