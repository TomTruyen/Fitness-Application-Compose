package com.tomtruyen.feature.settings

import com.tomtruyen.core.common.ThemeMode
import com.tomtruyen.core.common.models.UnitType

sealed class SettingsUiAction {
    data class OnUnitChanged(val value: UnitType) : SettingsUiAction()

    data class OnRestChanged(val value: Int) : SettingsUiAction()

    data class OnRestEnabledChanged(val value: Boolean) : SettingsUiAction()

    data class OnRestVibrationEnabledChanged(val value: Boolean) : SettingsUiAction()

    data object Refresh : SettingsUiAction()

    data object Logout : SettingsUiAction()

    data class OnThemeModeChanged(val mode: ThemeMode) : SettingsUiAction()

    sealed class Sheet : SettingsUiAction() {
        sealed class WeightUnit : Sheet() {
            data object Show : WeightUnit()

            data object Dismiss : WeightUnit()
        }

        sealed class RestTime : Sheet() {
            data object Show : RestTime()

            data object Dismiss : RestTime()
        }

        sealed class ThemeMode : Sheet() {
            data object Show : ThemeMode()

            data object Dismiss : ThemeMode()
        }
    }
}
