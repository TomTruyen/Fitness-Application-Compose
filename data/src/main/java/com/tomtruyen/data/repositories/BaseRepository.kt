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
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

abstract class BaseRepository(
): KoinComponent {
    abstract val identifier: String

    internal val supabase: SupabaseClient by inject(SupabaseClient::class.java)

    val context: Context by inject(Context::class.java)

    internal val database: AppDatabase by inject(AppDatabase::class.java)
    private val cacheDao: CacheTTLDao by inject(CacheTTLDao::class.java)

    protected val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend fun transaction(block: suspend () -> Unit) = database.withTransaction {
        block()
    }

    fun launchWithTransaction(block: suspend () -> Unit) = scope.launch {
        transaction(block)
    }

    fun launchWithCacheTransactions(overrideIdentifier: String? = null, block: suspend () -> Unit) = launchWithTransaction {
        block()

        val cacheKey = overrideIdentifier ?: identifier
        cacheDao.save(CacheTTL(cacheKey))
    }

    protected suspend fun fetch(
        refresh: Boolean = false,
        overrideIdentifier: String? = null,
        block: suspend () -> Unit
    ) {
        val cacheKey = overrideIdentifier ?: identifier
        Log.i(TAG, "Fetching data for $cacheKey from Firebase... (Checking Cache first)")

        val isCacheExpired = cacheDao.findById(cacheKey)?.isExpired ?: true

        if(isCacheExpired || refresh) {
            Log.i(TAG, "Cache is expired or refresh is true, fetching $cacheKey from Firebase")

            block()
            return
        }

        Log.i(TAG, "Cache is not expired. Skipping Firebase fetch for $cacheKey")
    }

    companion object {
        private const val TAG = "BaseRepository"
    }
}