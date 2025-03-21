package com.tomtruyen.feature.settings.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.feature.settings.SettingsUiAction
import com.tomtruyen.feature.settings.SettingsUiState

class SheetStateManager(
    private val updateState: ((SettingsUiState) -> SettingsUiState) -> Unit
) : StateManager<SettingsUiAction.Sheet> {
    private fun showWeightUnitSheet(show: Boolean) = updateState {
        it.copy(
            showWeightUnitSheet = show
        )
    }

    private fun showRestTimeSheet(show: Boolean) = updateState {
        it.copy(
            showRestTimeSheet = show
        )
    }

    private fun showThemeModeSheet(show: Boolean) = updateState {
        it.copy(
            showThemeModeSheet = show
        )
    }

    override fun onAction(action: SettingsUiAction.Sheet) {
        when (action) {
            SettingsUiAction.Sheet.ThemeMode.Show -> showThemeModeSheet(true)

            SettingsUiAction.Sheet.ThemeMode.Dismiss -> showThemeModeSheet(false)

            SettingsUiAction.Sheet.WeightUnit.Show -> showWeightUnitSheet(true)

            SettingsUiAction.Sheet.WeightUnit.Dismiss -> showWeightUnitSheet(false)

            SettingsUiAction.Sheet.RestTime.Show -> showRestTimeSheet(true)

            SettingsUiAction.Sheet.RestTime.Dismiss -> showRestTimeSheet(false)
        }
    }
}