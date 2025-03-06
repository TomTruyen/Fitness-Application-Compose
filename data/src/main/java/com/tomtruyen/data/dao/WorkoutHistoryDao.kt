package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutHistory
import com.tomtruyen.data.entities.WorkoutHistoryWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {
    @Transaction
    @Query("SELECT * FROM ${WorkoutHistory.TABLE_NAME} ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    fun findHistoriesAsync(
        offset: Int,
        limit: Int = WorkoutHistory.PAGE_SIZE
    ): Flow<List<WorkoutHistoryWithExercises>>

    @Upsert
    suspend fun save(workoutHistory: WorkoutHistory): Long

    @Upsert
    suspend fun saveAll(workoutHistories: List<WorkoutHistory>): List<Long>

    @Query("DELETE FROM ${WorkoutHistory.TABLE_NAME}")
    suspend fun deleteAll(): Int
}