package com.tomtruyen.feature.profile

import com.tomtruyen.data.entities.Settings
import androidx.compose.runtime.Immutable

@Immutable
data class ProfileUiState(
    val initialSettings: Settings? = null,
    val settings: Settings = Settings(),

    val loading: Boolean = false,
    val refreshing: Boolean = false,
)
