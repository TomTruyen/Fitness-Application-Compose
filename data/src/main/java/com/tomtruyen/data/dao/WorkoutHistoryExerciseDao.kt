package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutHistoryExercise

@Dao
fun interface WorkoutHistoryExerciseDao {
    @Upsert
    suspend fun saveAll(exercises: List<WorkoutHistoryExercise>): List<Long>
}