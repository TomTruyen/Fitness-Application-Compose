package com.tomtruyen.data.firebase.models

interface FirebaseCallback<T> {
    fun onSuccess(value: T) {}
    fun onError(error: String? = null) {}
    fun onCancel() {}
    fun onStopLoading() {}
}