package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.models.ExerciseFilter
import com.tomtruyen.data.repositories.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class ExerciseRepository : BaseRepository() {
    override val identifier: String
        get() = Exercise.TABLE_NAME

    abstract fun findExercises(
        query: String,
        filter: ExerciseFilter
    ): Flow<List<ExerciseWithCategoryAndEquipment>>

    abstract suspend fun findExerciseById(id: String): ExerciseWithCategoryAndEquipment?
    abstract fun findExerciseByIdAsync(id: String): Flow<ExerciseWithCategoryAndEquipment?>
    abstract suspend fun getExercises(userId: String?, refresh: Boolean)
    abstract suspend fun saveExercise(
        userId: String,
        userExercise: ExerciseWithCategoryAndEquipment
    )

    abstract suspend fun deleteExercise(userId: String, exerciseId: String)
}