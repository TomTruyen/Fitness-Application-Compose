package com.tomtruyen.data.repositories

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import com.tomtruyen.data.AppDatabase
import com.tomtruyen.data.dao.SyncCacheDao
import com.tomtruyen.data.entities.BaseEntity
import com.tomtruyen.data.entities.SyncCache
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

abstract class BaseRepository : KoinComponent {
    abstract val cacheKey: String

    val context: Context by inject(Context::class.java)

    protected val database: AppDatabase by inject(AppDatabase::class.java)
    protected val cacheDao: SyncCacheDao by inject(SyncCacheDao::class.java)
    internal val supabase: SupabaseClient by inject(SupabaseClient::class.java)

    protected val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend fun transaction(block: suspend () -> Unit) {
        database.withTransaction(block)
    }

    suspend fun cacheTransaction(
        pageCacheKey: String? = null,
        block: suspend () -> Unit
    ) = transaction {
        block()

        cacheDao.save(SyncCache(pageCacheKey ?: cacheKey))
    }

    protected suspend fun <T> fetch(
        refresh: Boolean = false,
        pageCacheKey: String? = null,
        block: suspend () -> T
    ): T? {
        val cacheKey = pageCacheKey ?: cacheKey

        Log.i(TAG, "Checking cache for $cacheKey")

        // Synced means that we fetched it, after that we just use Workers to sync in background
        // We want to go offline first for almost everything
        val isSynced = cacheDao.findById(cacheKey) != null

        if (!isSynced || refresh) {
            Log.i(
                TAG,
                "SyncCache entry does not exist or forcefully refreshing, fetching $cacheKey from Supabase"
            )

            return block()
        }
        Log.i(TAG, "SyncCache entry exists. Skipping Supabase fetch for $cacheKey")

        return null
    }

    /**
     * Deletes any entities that are not in the list.
     *
     * This can be used to remove items that have a Foreign Key that we deleted locally,
     * without requiring us to delete the parent entity to CASCADE it.
     * This manually checks and deletes the dangling items
     */
    suspend fun deleteSupabaseDangling(
        table: String,
        key: String,
        entitiesToKeep: List<BaseEntity>,
        foreignKeyConstraint: FilterOperation
    ) {
        val ids = entitiesToKeep.map { it.id }

        if (ids.isEmpty()) return

        supabase.from(table).delete {
            filter {
                and {
                    filterNot(key, FilterOperator.IN, "(${ids.joinToString(",")})")
                    filter(foreignKeyConstraint)
                }
            }
        }
    }

    protected suspend fun findMissingCacheKeys(
        refresh: Boolean,
        cacheKeys: List<String>
    ): Set<String> {
        if(refresh) return cacheKeys.toSet()

        val syncCacheKeys = cacheDao.findAll()

        return cacheKeys.filter() {
            !syncCacheKeys.contains(it)
        }.toSet()
    }

    companion object {
        private const val TAG = "BaseRepository"
    }
}