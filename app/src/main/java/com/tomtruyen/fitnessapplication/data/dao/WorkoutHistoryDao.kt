package com.tomtruyen.fitnessapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistory
import com.tomtruyen.fitnessapplication.data.entities.WorkoutHistoryWithWorkout

@Dao
abstract class WorkoutHistoryDao {
    @Transaction
    @Query("SELECT * FROM workout_history WHERE createdAt BETWEEN :start AND :end")
    abstract fun findWorkoutHistoriesByRange(
        start: Long,
        end: Long
    ): List<WorkoutHistoryWithWorkout>

    @Upsert
    abstract fun saveAll(histories: List<WorkoutHistory>): List<Long>
}