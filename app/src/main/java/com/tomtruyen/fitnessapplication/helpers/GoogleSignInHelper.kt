package com.tomtruyen.fitnessapplication.helpers

import com.tomtruyen.fitnessapplication.BuildConfig
import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity

object GoogleSignInHelper {
    fun getGoogleSignInClient(context: Context) = Identity.getSignInClient(context)

    fun getGoogleSignInRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()
}