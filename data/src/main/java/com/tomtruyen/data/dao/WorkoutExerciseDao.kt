package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutExercise

@Dao
fun interface WorkoutExerciseDao {
    @Upsert
    suspend fun saveAll(workoutExercises: List<WorkoutExercise>): List<Long>
}