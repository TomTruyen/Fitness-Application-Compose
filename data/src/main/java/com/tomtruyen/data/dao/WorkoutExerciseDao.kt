package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutExercise

@Dao
abstract class WorkoutExerciseDao {
    @Upsert
    abstract fun saveAll(workoutExercises: List<WorkoutExercise>): List<Long>
}