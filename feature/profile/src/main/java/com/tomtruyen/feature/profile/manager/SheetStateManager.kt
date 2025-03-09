package com.tomtruyen.feature.profile.manager

import com.tomtruyen.core.common.manager.StateManager
import com.tomtruyen.feature.profile.ProfileUiAction
import com.tomtruyen.feature.profile.ProfileUiState

class SheetStateManager(
    private val updateState: ((ProfileUiState) -> ProfileUiState) -> Unit
): StateManager<ProfileUiAction.Sheet> {
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

    override fun onAction(action: ProfileUiAction.Sheet) {
        when(action) {
            ProfileUiAction.Sheet.ThemeMode.Show -> showThemeModeSheet(true)

            ProfileUiAction.Sheet.ThemeMode.Dismiss -> showThemeModeSheet(false)

            ProfileUiAction.Sheet.WeightUnit.Show -> showWeightUnitSheet(true)

            ProfileUiAction.Sheet.WeightUnit.Dismiss -> showWeightUnitSheet(false)

            ProfileUiAction.Sheet.RestTime.Show -> showRestTimeSheet(true)

            ProfileUiAction.Sheet.RestTime.Dismiss -> showRestTimeSheet(false)
        }
    }
}