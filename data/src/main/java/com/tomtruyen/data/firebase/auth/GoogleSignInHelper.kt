package com.tomtruyen.data.firebase.auth

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity

object GoogleSignInHelper {
    fun getGoogleSignInClient(context: Context) = Identity.getSignInClient(context)

    fun getGoogleSignInRequest(serverClientId: String) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(serverClientId)
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()
}