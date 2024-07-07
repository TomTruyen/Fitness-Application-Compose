package com.tomtruyen.fitnessapplication.providers

import com.tomtruyen.models.providers.CredentialProvider

data class CredentialProviderImpl(
    override val googleServerClientId: String
) : CredentialProvider