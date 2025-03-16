package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.PreviousSet
import com.tomtruyen.data.entities.SyncCache
import com.tomtruyen.data.repositories.interfaces.PreviousSetRepository
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class PreviousSetRepositoryImpl: PreviousSetRepository() {
    private val exerciseDao = database.exerciseDao()

    override suspend fun findPreviousSets() = dao.findAllAsync()

    override suspend fun getPreviousSetsForExercises() {
        val result = supabase.postgrest.rpc(
            function = PreviousSet.RPC_FUNCTION,
            parameters = JsonObject(
                mapOf(
                    PreviousSet.EXERCISE_ID_PARAM to JsonArray(
                        content = findMissingCacheKeys().distinct().map { id ->
                            JsonPrimitive(id)
                        }
                    )
                )
            )
        )

        val sets = result.decodeList<PreviousSet>()

        transaction {
            dao.saveAll(sets)

            cacheDao.saveAll(
                cache = sets.map {
                    SyncCache(
                        id = PreviousSet.createCacheKey(it.exerciseId)
                    )
                }
            )
        }
    }

    private suspend fun findMissingCacheKeys(): List<String> {
        val exerciseIds = exerciseDao.findAllIds()

        val cacheKeys = exerciseIds.map(PreviousSet::createCacheKey)

        val syncCacheKeys = cacheDao.findAll()

        return cacheKeys.filter {
            !syncCacheKeys.contains(it)
        }.mapNotNull(PreviousSet::extractExerciseId)
    }
}