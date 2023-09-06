package com.tomtruyen.fitnessapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistory
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistoryWithWorkout
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WorkoutHistoryDao {
    @Transaction
    @Query("SELECT * FROM ${WorkoutHistory.TABLE_NAME} WHERE createdAt BETWEEN :start AND :end")
    abstract fun findWorkoutHistoriesByRange(
        start: Long,
        end: Long
    ): List<WorkoutHistoryWithWorkout>

    @Upsert
    abstract fun saveAll(histories: List<WorkoutHistory>): List<Long>

    @Query("SELECT * FROM ${WorkoutHistory.TABLE_NAME} WHERE id IN (:ids)")
    abstract fun findWorkoutHistoriesByIds(ids: List<String>): List<WorkoutHistoryWithWorkout>
}