package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutExerciseSet

@Dao
fun interface WorkoutExerciseSetDao {
    @Upsert
    suspend fun saveAll(sets: List<WorkoutExerciseSet>): List<Long>
}