package com.tomtruyen.fitnessapplication.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.tomtruyen.fitnessapplication.data.entities.WorkoutExercise

@Dao
abstract class WorkoutExerciseDao {
    @Upsert
    abstract fun saveAll(workoutExercises: List<WorkoutExercise>): List<Long>
}