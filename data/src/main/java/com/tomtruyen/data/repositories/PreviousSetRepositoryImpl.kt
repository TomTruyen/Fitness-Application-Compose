package com.tomtruyen.data.repositories

import android.util.Log
import com.tomtruyen.data.entities.PreviousSet
import com.tomtruyen.data.entities.SyncCache
import com.tomtruyen.data.repositories.interfaces.PreviousSetRepository
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class PreviousSetRepositoryImpl : PreviousSetRepository() {
    private val exerciseDao = database.exerciseDao()

    override suspend fun findPreviousSets() = dao.findAllAsync()

    override suspend fun getPreviousSetsForExercises(refresh: Boolean) {
        val cacheKeys = findMissingCacheKeys(refresh)

        if(cacheKeys.isEmpty()) return

        val result = supabase.postgrest.rpc(
            function = PreviousSet.RPC_FUNCTION,
            parameters = JsonObject(
                mapOf(
                    PreviousSet.EXERCISE_ID_PARAM to JsonArray(
                        content = cacheKeys.map { key ->
                            JsonPrimitive(PreviousSet.extractExerciseId(key))
                        }
                    )
                )
            )
        )

        val sets = result.decodeList<PreviousSet>()

        transaction {
            dao.saveAll(sets)

            cacheDao.saveAll(
                // Save all CacheKeys even if no sets were returned since we simply know there are none
                cache = cacheKeys.map(::SyncCache)
            )
        }
    }

    private suspend fun findMissingCacheKeys(refresh: Boolean): Set<String> {
        val exerciseIds = exerciseDao.findAllIds()

        val cacheKeys = exerciseIds.map(PreviousSet::createCacheKey)

        if(refresh) return cacheKeys.toSet()

        val syncCacheKeys = cacheDao.findAll()

        return cacheKeys.filter() {
            !syncCacheKeys.contains(it)
        }.toSet()
    }
}