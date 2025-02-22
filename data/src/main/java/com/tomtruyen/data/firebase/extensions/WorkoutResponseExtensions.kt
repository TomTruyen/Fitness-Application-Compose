package com.tomtruyen.data.firebase.extensions

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.firebase.models.WorkoutResponse

fun WorkoutResponse.weightExercises() = exercises.filter {
    it.exercise.typeEnum == Exercise.ExerciseType.WEIGHT
}

fun WorkoutResponse.totalWeight() = weightExercises().sumOf { exercise ->
    exercise.sets.sumOf { set ->
        set.weight ?: 0.0
    }
}

fun WorkoutResponse.repsCount() = weightExercises().sumOf { exercise ->
    exercise.sets.sumOf { set ->
        set.reps ?: 0
    }
}

fun WorkoutResponse.setCount() = weightExercises().sumOf { exercise ->
    exercise.sets.size
}

fun WorkoutResponse.totalVolume() = repsCount() * totalWeight()