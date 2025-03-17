package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.tomtruyen.data.entities.Workout
import com.tomtruyen.data.entities.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WorkoutDao : SyncDao<WorkoutWithExercises>(Workout.TABLE_NAME) {
    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} ORDER BY sortOrder ASC, createdAt DESC")
    abstract fun findWorkoutsAsync(): Flow<List<WorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} ORDER BY sortOrder ASC, createdAt DESC")
    abstract suspend fun findWorkouts(): List<WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE id = :id")
    abstract fun findByIdAsync(id: String): Flow<WorkoutWithExercises?>

    @Transaction
    @Query("SELECT * FROM ${Workout.TABLE_NAME} WHERE id = :id")
    abstract suspend fun findById(id: String): WorkoutWithExercises?

    @Query("DELETE FROM ${Workout.TABLE_NAME} WHERE id NOT IN (:ids) AND synced = :synced")
    abstract suspend fun deleteAllWorkoutsExcept(
        ids: List<String>,
        synced: Boolean = true
    ): Int

    @Upsert
    abstract suspend fun save(workout: Workout): Long

    @Upsert
    abstract suspend fun saveAll(workouts: List<Workout>): List<Long>

    @Query("DELETE FROM ${Workout.TABLE_NAME} WHERE id = :workoutId")
    abstract suspend fun deleteById(workoutId: String): Int
}