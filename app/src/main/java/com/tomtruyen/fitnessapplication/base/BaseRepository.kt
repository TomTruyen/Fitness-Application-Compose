package com.tomtruyen.fitnessapplication.base

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class BaseRepository {
    protected val db = Firebase.firestore
    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
}