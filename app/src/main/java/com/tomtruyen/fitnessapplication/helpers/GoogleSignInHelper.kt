package com.tomtruyen.fitnessapplication.helpers

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity

object GoogleSignInHelper {
    fun getGoogleSignInClient(context: Context) = Identity.getSignInClient(context)

    fun getGoogleSignInRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId()
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
}