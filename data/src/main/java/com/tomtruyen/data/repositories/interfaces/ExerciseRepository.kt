package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.repositories.BaseRepository
import com.tomtruyen.data.entities.Exercise
import kotlinx.coroutines.flow.Flow

abstract class ExerciseRepository: BaseRepository() {
    override val identifier: String
        get() = Exercise.TABLE_NAME

    abstract fun findExercises(query: String, filter: com.tomtruyen.models.ExerciseFilter): Flow<List<Exercise>>
    abstract fun findExerciseById(id: String): Flow<Exercise?>
    abstract fun findEquipment(): Flow<List<String>>
    abstract fun findCategories(): Flow<List<String>>
    abstract suspend fun getExercises(userId: String?, refresh: Boolean)
    abstract suspend fun saveUserExercise(userId: String, exercise: Exercise)
    abstract suspend fun deleteUserExercise(userId: String, exerciseId: String)
}