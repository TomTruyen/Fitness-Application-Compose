package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutHistory

@Dao
interface WorkoutHistoryDao {
    @Upsert
    suspend fun save(workoutHistory: WorkoutHistory): Long

    @Upsert
    suspend fun saveAll(workoutHistories: List<WorkoutHistory>): List<Long>

    @Query("DELETE FROM ${WorkoutHistory.TABLE_NAME}")
    suspend fun deleteAll(): Int
}