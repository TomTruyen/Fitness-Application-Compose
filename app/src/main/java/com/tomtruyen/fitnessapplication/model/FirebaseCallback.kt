package com.tomtruyen.fitnessapplication.model

interface FirebaseCallback<T> {
    fun onSuccess(value: T)
    fun onError(error: String? = null)
    fun onCancel() {}
}