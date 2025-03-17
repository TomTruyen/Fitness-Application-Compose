package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.PreviousSet
import com.tomtruyen.data.repositories.BaseRepository

abstract class PreviousSetRepository : BaseRepository() {
    override val cacheKey: String
        get() = PreviousSet.TABLE_NAME

    val dao = database.previousSetDao()

    abstract suspend fun findPreviousSets(): List<PreviousSet>

    abstract suspend fun getPreviousSetsForExercises(refresh: Boolean)
}