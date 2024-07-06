package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutHistoryWithWorkout

@Dao
abstract class WorkoutHistoryDao {
    @Transaction
    @Query("SELECT * FROM ${WorkoutHistory.TABLE_NAME} WHERE createdAt BETWEEN :start AND :end ORDER BY createdAt DESC")
    abstract fun findWorkoutHistoriesByRange(
        start: Long,
        end: Long
    ): List<WorkoutHistoryWithWorkout>

    @Upsert
    abstract fun saveAll(histories: List<WorkoutHistory>): List<Long>

    @Transaction
    @Query("SELECT * FROM ${WorkoutHistory.TABLE_NAME} WHERE id IN (:ids) ORDER BY createdAt DESC")
    abstract fun findWorkoutHistoriesByIds(ids: List<String>): List<WorkoutHistoryWithWorkout>
}