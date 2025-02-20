package com.tomtruyen.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.tomtruyen.data.entities.WorkoutSet

@Dao
fun interface WorkoutSetDao {
    @Upsert
    fun saveAll(sets: List<WorkoutSet>): List<Long>
}