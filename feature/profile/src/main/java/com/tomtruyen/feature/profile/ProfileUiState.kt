package com.tomtruyen.feature.profile

import com.tomtruyen.data.entities.Settings

data class ProfileUiState(
    val initialSettings: com.tomtruyen.data.entities.Settings? = null,
    val settings: com.tomtruyen.data.entities.Settings = com.tomtruyen.data.entities.Settings(),

    val loading: Boolean = false,
)
