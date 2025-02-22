package com.tomtruyen.data.firebase.extensions

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.models.WorkoutResponse

fun WorkoutResponse.weightExercises() = exercises.filter {
    it.exercise.typeEnum == Exercise.ExerciseType.WEIGHT
}

fun WorkoutResponse.totalWeightCompleted() = weightExercises().sumOf { exercise ->
    exercise.sets.filter { it.completed }.sumOf { set ->
        set.weight ?: 0.0
    }
}

fun WorkoutResponse.repsCountCompleted() = weightExercises().sumOf { exercise ->
    exercise.sets.filter { it.completed }.sumOf { set ->
        set.reps ?: 0
    }
}

fun WorkoutResponse.setCountCompleted() = weightExercises().sumOf { exercise ->
    exercise.sets.filter { it.completed }.size
}

fun WorkoutResponse.totalVolumeCompleted() = repsCountCompleted() * totalWeightCompleted()