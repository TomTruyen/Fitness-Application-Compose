package com.tomtruyen.fitnessapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tomtruyen.fitnessapplication.data.entities.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ExerciseDao {
    @Query("SELECT * FROM ${Exercise.TABLE_NAME}")
    abstract fun findAllAsync(): Flow<List<Exercise>>

    @Upsert
    abstract fun saveAll(exercises: List<Exercise>): List<Long>
}