package com.tomtruyen.data.repositories

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import com.tomtruyen.data.AppDatabase
import com.tomtruyen.data.dao.CacheTTLDao
import com.tomtruyen.data.entities.CacheTTL
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

abstract class BaseRepository(
) : KoinComponent {
    abstract val cacheKey: String

    val context: Context by inject(Context::class.java)

    protected val database: AppDatabase by inject(AppDatabase::class.java)
    protected val cacheDao: CacheTTLDao by inject(CacheTTLDao::class.java)
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

        cacheDao.save(CacheTTL(pageCacheKey ?: cacheKey))
    }

    protected suspend fun <T> fetch(
        refresh: Boolean = false,
        pageCacheKey: String? = null,
        block: suspend () -> T
    ): T? {
        val cacheKey = pageCacheKey ?: cacheKey

        Log.i(TAG, "Fetching data for $cacheKey from Supabase... (Checking Cache first)")

        val isCacheExpired = cacheDao.findById(cacheKey)?.isExpired ?: true

        if (isCacheExpired || refresh) {
            Log.i(TAG, "Cache is expired or refresh is true, fetching $cacheKey from Supabase")

            return block()
        }
        Log.i(TAG, "Cache is not expired. Skipping Supabase fetch for $cacheKey")

        return null
    }

    companion object {
        private const val TAG = "BaseRepository"
    }
}