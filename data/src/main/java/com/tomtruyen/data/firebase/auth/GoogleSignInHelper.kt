package com.tomtruyen.data.firebase.auth

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.tomtruyen.models.providers.CredentialProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object GoogleSignInHelper: KoinComponent {
    private val credentialProvider: CredentialProvider by inject()

    fun getGoogleSignInClient(context: Context) = Identity.getSignInClient(context)

    fun getGoogleSignInRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(credentialProvider.googleServerClientId)
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()
}