package com.tomtruyen.fitoryx.providers

import com.tomtruyen.core.common.providers.BuildConfigFieldProvider

data class BuildConfigFieldProviderImpl(
    override val versionName: String,
    override val versionCode: Int
) : BuildConfigFieldProvider