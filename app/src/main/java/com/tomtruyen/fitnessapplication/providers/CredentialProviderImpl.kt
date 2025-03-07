package com.tomtruyen.Fynix.providers

import com.tomtruyen.core.common.providers.CredentialProvider

data class CredentialProviderImpl(
    override val googleServerClientId: String
) : CredentialProvider