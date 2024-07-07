package com.tomtruyen.fitnessapplication.providers

import com.tomtruyen.models.providers.BuildConfigFieldProvider

data class BuildConfigFieldProviderImpl(
    override val versionName: String,
    override val versionCode: Int
) : BuildConfigFieldProvider