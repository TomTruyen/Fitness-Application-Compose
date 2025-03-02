package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutHistory

@Dao
interface WorkoutHistoryDao {
    @Upsert
    suspend fun save(workoutHistory: WorkoutHistory): Long
}