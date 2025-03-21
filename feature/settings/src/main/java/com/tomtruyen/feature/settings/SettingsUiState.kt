package com.tomtruyen.feature.settings

import androidx.compose.runtime.Immutable
import com.tomtruyen.data.models.ui.SettingsUiModel

@Immutable
data class SettingsUiState(
    val initialSettings: SettingsUiModel? = null,
    val settings: SettingsUiModel = SettingsUiModel(),

    val showWeightUnitSheet: Boolean = false,
    val showRestTimeSheet: Boolean = false,
    val showThemeModeSheet: Boolean = false,

    val loading: Boolean = false,
    val refreshing: Boolean = false,
)
