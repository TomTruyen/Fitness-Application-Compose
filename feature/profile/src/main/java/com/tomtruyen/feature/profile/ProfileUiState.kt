package com.tomtruyen.feature.profile

import com.tomtruyen.data.entities.Settings

data class ProfileUiState(
    val initialSettings: Settings? = null,
    val settings: Settings = Settings(),

    val loading: Boolean = false,
    val refreshing: Boolean = false,
)
