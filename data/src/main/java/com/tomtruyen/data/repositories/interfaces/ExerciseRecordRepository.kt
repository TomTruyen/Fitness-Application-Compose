package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.ExerciseRecord
import com.tomtruyen.data.repositories.BaseRepository

abstract class ExerciseRecordRepository: BaseRepository() {
    override val cacheKey: String
        get() = ExerciseRecord.TABLE_NAME

    val dao = database.exerciseRecordDao()

    abstract suspend fun getExerciseRecordsForExercises(refresh: Boolean)
}