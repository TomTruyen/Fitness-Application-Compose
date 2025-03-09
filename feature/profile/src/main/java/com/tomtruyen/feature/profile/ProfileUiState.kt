package com.tomtruyen.feature.profile

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.models.ui.SettingsUiModel

@Immutable
data class ProfileUiState(
    val initialSettings: SettingsUiModel? = null,
    val settings: SettingsUiModel = SettingsUiModel(),

    val showWeightUnitSheet: Boolean = false,
    val showThemeModeSheet: Boolean = false,

    val loading: Boolean = false,
    val refreshing: Boolean = false,
)
