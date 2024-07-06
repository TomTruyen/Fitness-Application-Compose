package com.tomtruyen.data.firebase.extensions

import android.content.Context
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.tomtruyen.core.common.R as CommonR

fun Exception?.toFirebaseExceptionFormat(context: Context) = when(this) {
    is FirebaseAuthWeakPasswordException -> context.getString(CommonR.string.error_weak_password)
    is FirebaseAuthInvalidUserException -> context.getString(CommonR.string.error_wrong_credentials)
    is FirebaseAuthUserCollisionException -> context.getString(CommonR.string.error_email_already_exists)
    else -> this?.message
}