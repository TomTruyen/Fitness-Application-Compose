package com.tomtruyen.fitnessapplication.base

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tomtruyen.fitnessapplication.AppGlobals
import com.tomtruyen.fitnessapplication.FetchedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.java.KoinJavaComponent.inject

open class BaseRepository {
    private val globals: AppGlobals by inject(AppGlobals::class.java)

    protected val db = Firebase.firestore
    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // This function is used to check if we have already made a request to Firebase.
    // If we have, we don't need to make another request --> Reduces the amount of Reads on Firebase
    protected fun tryRequestWhenNotFetched(type: FetchedData.Type? = null, force: Boolean = false, block: () -> Unit) {
        if(type == null || force) {
            block()
            return
        }

        if(!globals.fetchedData.hasFetched(type)) {
            block()
        }
    }

    protected fun setFetchSuccessful(type: FetchedData.Type?) {
        if(type == null) return

        globals.fetchedData.setFetched(type, true)
    }
}