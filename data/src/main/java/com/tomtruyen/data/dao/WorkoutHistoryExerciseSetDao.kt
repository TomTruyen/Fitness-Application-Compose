package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet

@Dao
fun interface WorkoutHistoryExerciseSetDao {
    @Upsert
    suspend fun saveAll(sets: List<WorkoutHistoryExerciseSet>): List<Long>
}