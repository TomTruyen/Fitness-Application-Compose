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
    @Query("SELECT * FROM ${Workout.TABLE_NAME} ORDER BY createdAt DESC")
    fun findWorkoutsAsync(): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} ORDER BY createdAt DESC")
    suspend fun findWorkouts(): List<WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE id = :id")
    fun findByIdAsync(id: String): Flow<WorkoutWithExercises?>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE id = :id")
    suspend fun findById(id: String): WorkoutWithExercises?

    @Query("DELETE FROM ${Workout.TABLE_NAME}")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM ${Workout.TABLE_NAME} WHERE id NOT IN (:ids)")
    suspend fun deleteAllWorkoutsExcept(ids: List<String>): Int

    @Upsert
    suspend fun save(workout: Workout): Long

    @Upsert
    suspend fun saveAll(workouts: List<Workout>): List<Long>

    @Query("DELETE FROM ${Workout.TABLE_NAME} WHERE id = :workoutId")
    suspend fun deleteById(workoutId: String): Int
}