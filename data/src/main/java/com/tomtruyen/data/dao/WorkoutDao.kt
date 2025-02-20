package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE isPerformed = 0 ORDER BY createdAt DESC")
    fun findWorkoutsAsync(): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE isPerformed = 0 ORDER BY createdAt DESC")
    fun findWorkouts(): List<WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE id = :id")
    fun findByIdAsync(id: String): Flow<WorkoutWithExercises?>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE id = :id")
    fun findById(id: String): WorkoutWithExercises?

    @Query("DELETE FROM ${Workout.TABLE_NAME}")
    fun deleteAll(): Int

    @Query("DELETE FROM ${Workout.TABLE_NAME} WHERE id NOT IN (:ids)")
    fun deleteAllWorkoutsExcept(ids: List<String>): Int

    @Upsert
    fun saveAll(workouts: List<Workout>): List<Long>

    @Query("DELETE FROM ${Workout.TABLE_NAME} WHERE id = :workoutId")
    fun deleteById(workoutId: String): Int
}