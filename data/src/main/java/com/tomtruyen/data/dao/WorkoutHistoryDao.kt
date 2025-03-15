package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutHistoryWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WorkoutHistoryDao: SyncDao<WorkoutHistoryWithExercises>(WorkoutHistory.TABLE_NAME) {
    @Transaction
    @Query("SELECT * FROM ${WorkoutHistory.TABLE_NAME} WHERE id = :id")
    abstract fun findHistoryByIdAsync(id: String): Flow<WorkoutHistoryWithExercises?>

    @Transaction
    @Query("SELECT * FROM ${WorkoutHistory.TABLE_NAME} ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    abstract fun findHistoriesAsync(
        offset: Int,
        limit: Int = WorkoutHistory.PAGE_SIZE
    ): Flow<List<WorkoutHistoryWithExercises>>

    @Upsert
    abstract suspend fun save(workoutHistory: WorkoutHistory): Long

    @Upsert
    abstract suspend fun saveAll(workoutHistories: List<WorkoutHistory>): List<Long>

    @Query("DELETE FROM ${WorkoutHistory.TABLE_NAME} WHERE id = :id")
    abstract suspend fun deleteById(id: String): Int

    @Query("DELETE FROM ${WorkoutHistory.TABLE_NAME} WHERE synced = :synced")
    abstract suspend fun deleteAll(synced: Boolean = true): Int
}