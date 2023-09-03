package com.tomtruyen.fitnessapplication.base

import androidx.room.withTransaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tomtruyen.fitnessapplication.data.AppDatabase
import com.tomtruyen.fitnessapplication.helpers.GlobalProvider
import com.tomtruyen.fitnessapplication.model.FetchedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

open class BaseRepository {
    private val globalProvider: GlobalProvider by inject(GlobalProvider::class.java)

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
}