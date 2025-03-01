package com.tomtruyen.feature.profile

import com.tomtruyen.data.entities.Settings
import androidx.compose.runtime.Immutable
import com.tomtruyen.data.models.ui.SettingsUiModel

@Immutable
data class ProfileUiState(
    val initialSettings: SettingsUiModel? = null,
    val settings: SettingsUiModel = SettingsUiModel(),

    val loading: Boolean = false,
    val refreshing: Boolean = false,
)
