package com.tomtruyen.fitnessapplication.networking

import android.os.Parcelable
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import com.tomtruyen.fitnessapplication.data.entities.WorkoutSet
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class WorkoutResponse(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var exercises: List<WorkoutExerciseResponse> = emptyList()
): Parcelable

@Parcelize

data class WorkoutExerciseResponse(
    var id: String = UUID.randomUUID().toString(),
    var notes: String = "",
    var rest: Int = 0,
    var restEnabled: Boolean = false,
    var order: Int = 0,
    var exercise: Exercise = Exercise(),
    var sets: List<WorkoutSet> = emptyList()
): Parcelable