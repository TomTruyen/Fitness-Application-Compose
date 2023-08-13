package com.tomtruyen.fitnessapplication.repositories.interfaces

import com.tomtruyen.fitnessapplication.data.entities.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun findExercises(): Flow<List<Exercise>>
    suspend fun getExercises(): List<Exercise>
}