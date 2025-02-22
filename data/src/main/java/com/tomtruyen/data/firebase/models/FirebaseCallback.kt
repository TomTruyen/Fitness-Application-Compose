package com.tomtruyen.data.firebase.models

interface FirebaseCallback<T> {
    fun onSuccess(value: T) = Unit
    fun onError(error: String? = null) = Unit
    fun onCancel() = Unit
    fun onStopLoading() = Unit
}