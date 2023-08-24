package com.tomtruyen.fitnessapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tomtruyen.fitnessapplication.data.entities.Workout
import com.tomtruyen.fitnessapplication.data.entities.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WorkoutDao {
    @Query("SELECT * FROM ${Workout.TABLE_NAME} ORDER BY createdAt DESC")
    abstract fun findWorkoutsAsync(): Flow<List<WorkoutWithExercises>>

    @Query("SELECT * FROM ${Workout.TABLE_NAME} ORDER BY createdAt DESC")
    abstract fun findWorkouts(): List<WorkoutWithExercises>

    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE id = :id")
    abstract fun findByIdAsync(id: String): Flow<WorkoutWithExercises?>

    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE id = :id")
    abstract fun findById(id: String): WorkoutWithExercises?

    @Query("DELETE FROM ${Workout.TABLE_NAME}")
    abstract fun deleteAll(): Int

    @Query("DELETE FROM ${Workout.TABLE_NAME} WHERE id NOT IN (:ids)")
    abstract fun deleteAllWorkoutsExcept(ids: List<String>): Int

    @Upsert
    abstract fun saveAll(workouts: List<Workout>): List<Long>
}