package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WorkoutDao {
    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE isPerformed = 0 ORDER BY createdAt DESC")
    abstract fun findWorkoutsAsync(): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE isPerformed = 0 ORDER BY createdAt DESC")
    abstract fun findWorkouts(): List<WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE id = :id")
    abstract fun findByIdAsync(id: String): Flow<WorkoutWithExercises?>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE id = :id")
    abstract fun findById(id: String): WorkoutWithExercises?

    @Query("DELETE FROM ${Workout.TABLE_NAME}")
    abstract fun deleteAll(): Int

    @Query("DELETE FROM ${Workout.TABLE_NAME} WHERE id NOT IN (:ids)")
    abstract fun deleteAllWorkoutsExcept(ids: List<String>): Int

    @Upsert
    abstract fun saveAll(workouts: List<Workout>): List<Long>

    @Query("DELETE FROM ${Workout.TABLE_NAME} WHERE id = :workoutId")
    abstract fun deleteById(workoutId: String): Int
}