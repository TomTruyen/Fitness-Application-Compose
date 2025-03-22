package com.tomtruyen.core.common.models

import kotlinx.serialization.Serializable

// Used to determine if an input has changed in value in the UI
// Also present here to store for ActiveWorkout
@Serializable
enum class ChangeType {
    REP,
    WEIGHT,
    TIME;
}