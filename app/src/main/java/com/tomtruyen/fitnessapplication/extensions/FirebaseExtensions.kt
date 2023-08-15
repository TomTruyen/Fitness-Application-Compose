package com.tomtruyen.fitnessapplication.extensions

import android.content.Context
import com.google.android.gms.tasks.Task
import com.tomtruyen.fitnessapplication.helpers.FirebaseExceptionHelper
import com.tomtruyen.fitnessapplication.model.FirebaseCallback

fun <TResult, TEntity> Task<TResult>.handleCompletionResult(
    context: Context,
    setFetchSuccessful: (() -> Unit)? = null,
    callback: FirebaseCallback<TEntity>,
    onSuccess: (TResult) -> Unit
) {
    addOnCompleteListener { task ->
        callback.onStopLoading()

        if(task.isCanceled) {
            callback.onCancel()
            return@addOnCompleteListener
        }

        if(!task.isSuccessful) {
            callback.onError(FirebaseExceptionHelper.format(context, exception))
            return@addOnCompleteListener
        }

        setFetchSuccessful?.invoke()

        onSuccess(task.result)
    }
}