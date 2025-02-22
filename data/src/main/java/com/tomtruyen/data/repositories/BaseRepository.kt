package com.tomtruyen.data.repositories

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tomtruyen.data.AppDatabase
import com.tomtruyen.data.dao.CacheTTLDao
import com.tomtruyen.data.entities.CacheTTL
import com.tomtruyen.data.firebase.extensions.handleCompletionResult
import com.tomtruyen.data.firebase.models.FirebaseCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

abstract class BaseRepository(
): KoinComponent {
    abstract val identifier: String

    val context: Context by inject(Context::class.java)

    private val database: AppDatabase by inject(AppDatabase::class.java)
    private val cacheDao: CacheTTLDao by inject(CacheTTLDao::class.java)

    protected val db = Firebase.firestore
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
        onStopLoading: () -> Unit,
        block: () -> Unit
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
        
        onStopLoading()
    }

    protected suspend fun <T> updateOrSet(
        ref: DocumentReference,
        setData: Any,
        updateData: Any,
        field: String = "data",
        callback: FirebaseCallback<T>,
        onSuccess: (update: Boolean) -> Unit
    ) {
        val exists = ref.get().await().exists()

        val task = if(exists) {
            ref.update(field, updateData)
        } else {
            ref.set(setData)
        }

        task.handleCompletionResult(
            context = context,
            callback = callback,
        ) {
            onSuccess(exists)
        }
    }

    companion object {
        private const val TAG = "BaseRepository"
    }
}