package com.tomtruyen.fitnessapplication.helpers

import android.content.Context
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.tomtruyen.core.common.R as CommonR

object FirebaseExceptionHelper {
    fun format(context: Context, exception: Exception?) = when(exception) {
        is FirebaseAuthWeakPasswordException -> context.getString(CommonR.string.error_weak_password)
        is FirebaseAuthInvalidUserException -> context.getString(CommonR.string.error_wrong_credentials)
        is FirebaseAuthUserCollisionException -> context.getString(CommonR.string.error_email_already_exists)
        else -> exception?.message
    }
}