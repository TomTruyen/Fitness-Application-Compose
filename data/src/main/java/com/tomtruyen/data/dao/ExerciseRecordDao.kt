package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.tomtruyen.data.entities.ExerciseRecord

@Dao
interface ExerciseRecordDao {
    @Upsert
    suspend fun saveAll(records: List<ExerciseRecord>)
}