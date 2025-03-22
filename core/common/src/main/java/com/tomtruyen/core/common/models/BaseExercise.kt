package com.tomtruyen.core.common.models

interface BaseExercise {
    val id: String
    val imageUrl: String?
    val notes: String?
    val type: ExerciseType
    val sets: List<ExerciseSet>
    val displayName: String
}