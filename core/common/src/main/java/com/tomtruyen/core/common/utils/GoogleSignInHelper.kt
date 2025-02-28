package com.tomtruyen.core.common.utils

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.tomtruyen.core.common.providers.CredentialProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object GoogleSignInHelper : KoinComponent {
    private val credentialProvider: CredentialProvider by inject()

    fun getCredentialManager(context: Context) = CredentialManager.create(context)

    fun getGoogleSignInRequest() = GetCredentialRequest.Builder()
        .setPreferImmediatelyAvailableCredentials(false)
        .addCredentialOption(
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(credentialProvider.googleServerClientId)
                .setAutoSelectEnabled(true)
                .build()
        ).build()

    suspend fun signOut(context: Context) {
        getCredentialManager(context).clearCredentialState(
            ClearCredentialStateRequest(requestType = ClearCredentialStateRequest.TYPE_CLEAR_CREDENTIAL_STATE)
        )
    }
}