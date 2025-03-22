package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.ExerciseRecord
import com.tomtruyen.data.entities.SyncCache
import com.tomtruyen.data.repositories.interfaces.ExerciseRecordRepository
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class ExerciseRecordRepositoryImpl: ExerciseRecordRepository() {
    private val exerciseDao = database.exerciseDao()

    override suspend fun getExerciseRecordsForExercises(refresh: Boolean) {
        val cacheKeys = findMissingCacheKeys(refresh)

        if(cacheKeys.isEmpty()) return

        val result = supabase.postgrest.rpc(
            function = ExerciseRecord.RPC_FUNCTION,
            parameters = JsonObject(
                mapOf(
                    ExerciseRecord.EXERCISE_ID_PARAM to JsonArray(
                        content = cacheKeys.map { key ->
                            JsonPrimitive(ExerciseRecord.extractCacheId(key))
                        }
                    )
                )
            )
        )

        val records = result.decodeList<ExerciseRecord>()

        transaction {
            dao.saveAll(records)

            cacheDao.saveAll(
                // Save all CacheKeys even if no records were returned since we simply know there are none
                cache = cacheKeys.map(::SyncCache)
            )
        }
    }

    private suspend fun findMissingCacheKeys(refresh: Boolean): Set<String> {
        val exerciseIds = exerciseDao.findAllIds()

        val cacheKeys = exerciseIds.map(ExerciseRecord::createCacheKey)

        return super.findMissingCacheKeys(refresh, cacheKeys)
    }
}