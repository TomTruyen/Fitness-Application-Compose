package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.dao.ExerciseDao
import com.tomtruyen.data.dao.SyncDao
import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.ExerciseWithCategoryAndEquipment
import com.tomtruyen.data.models.ExerciseFilter
import com.tomtruyen.data.models.ui.ExerciseUiModel
import com.tomtruyen.data.repositories.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class ExerciseRepository : SyncRepository<Exercise>() {
    override val cacheKey: String
        get() = Exercise.TABLE_NAME

    override val dao = database.exerciseDao()

    abstract fun findExercises(
        query: String,
        filter: ExerciseFilter
    ): Flow<List<ExerciseUiModel>>

    abstract suspend fun findExerciseById(id: String): ExerciseUiModel?
    abstract fun findExerciseByIdAsync(id: String): Flow<ExerciseUiModel?>
    abstract suspend fun getExercises(userId: String?, refresh: Boolean)
    abstract suspend fun saveExercise(
        userId: String,
        exercise: ExerciseUiModel
    )

    abstract suspend fun deleteExercise(userId: String, exerciseId: String)
}