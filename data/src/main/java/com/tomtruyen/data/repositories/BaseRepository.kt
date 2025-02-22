package com.tomtruyen.data.repositories

import android.content.Context
import androidx.room.withTransaction
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tomtruyen.data.AppDatabase
import com.tomtruyen.data.firebase.extensions.handleCompletionResult
import com.tomtruyen.data.firebase.models.FirebaseCallback
import com.tomtruyen.models.DataFetchTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

open class BaseRepository(
    private val identifier: String? = null
): KoinComponent {
    val context: Context by inject(Context::class.java)

    private val database: AppDatabase by inject(AppDatabase::class.java)

    protected val db = Firebase.firestore
    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    protected suspend fun <T> transaction(block: suspend () -> T) {
        withContext(Dispatchers.IO) {
            database.withTransaction(block)
        }
    }

    fun launchWithTransaction(block: suspend () -> Unit) = scope.launch {
        transaction(block)
    }

    // This function is used to check if we have already made a request to Firebase.
    // If we have, we don't need to make another request --> Reduces the amount of Reads on Firebase
    protected fun tryRequestWhenNotFetched(
        overrideIdentifier: String? = null,
        onStopLoading: () -> Unit,
        force: Boolean = false,
        block: () -> Unit
    ) {
        val identifier = overrideIdentifier ?: identifier

        if(identifier == null || force || !DataFetchTracker.isFetched(identifier)) {
            block()
            return
        }

        onStopLoading()
    }

    protected fun setFetchSuccessful(overrideIdentifier: String? = null) {
        val identifier = overrideIdentifier ?: identifier

        identifier?.let {
            DataFetchTracker.fetched(it)
        }
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
}