package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.dao.SyncDao
import com.tomtruyen.data.repositories.BaseRepository

abstract class SyncRepository<T>: BaseRepository() {
    protected abstract val dao: SyncDao<T>

    abstract suspend fun sync(item: T)

    suspend fun findSyncItems() = dao.findSyncItems()
}