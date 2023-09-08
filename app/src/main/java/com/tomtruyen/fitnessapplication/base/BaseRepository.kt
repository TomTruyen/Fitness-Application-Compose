package com.tomtruyen.fitnessapplication.base

import androidx.room.withTransaction
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tomtruyen.fitnessapplication.data.AppDatabase
import com.tomtruyen.fitnessapplication.extensions.handleCompletionResult
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.FetchedData
import com.tomtruyen.fitnessapplication.model.FirebaseCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.java.KoinJavaComponent.inject

open class BaseRepository {
    protected val globalProvider: GlobalProvider by inject(GlobalProvider::class.java)

    private val database: AppDatabase by inject(AppDatabase::class.java)

    protected val db = Firebase.firestore
    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    protected suspend fun <T> transaction(block: suspend () -> T) = database.withTransaction(block)

    fun launchWithTransaction(block: suspend () -> Unit)  = scope.launch {
        transaction(block)
    }

    // This function is used to check if we have already made a request to Firebase.
    // If we have, we don't need to make another request --> Reduces the amount of Reads on Firebase
    protected fun tryRequestWhenNotFetched(identifier: String? = null, onStopLoading: () -> Unit, force: Boolean = false, block: () -> Unit) {
        if(identifier == null || force) {
            block()
            return
        }

        if(!globalProvider.fetchedData.hasFetched(identifier)) {
            block()
            return
        }

        onStopLoading()
    }

    protected fun setFetchSuccessful(identifier: String?) {
        if(identifier == null) return

        globalProvider.fetchedData.setFetched(identifier, true)
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
            context = globalProvider.context,
            callback = callback,
        ) {
            onSuccess(exists)
        }
    }
}