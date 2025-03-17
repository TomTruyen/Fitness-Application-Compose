package com.tomtruyen.feature.profile

import com.tomtruyen.core.common.ThemeMode
import com.tomtruyen.core.common.models.UnitType

sealed class ProfileUiAction {
    data class OnUnitChanged(val value: UnitType) : ProfileUiAction()

    data class OnRestChanged(val value: Int) : ProfileUiAction()

    data class OnRestEnabledChanged(val value: Boolean) : ProfileUiAction()

    data class OnRestVibrationEnabledChanged(val value: Boolean) : ProfileUiAction()

    data object Refresh : ProfileUiAction()

    data object Logout : ProfileUiAction()

    data class OnThemeModeChanged(val mode: ThemeMode) : ProfileUiAction()

    sealed class Sheet : ProfileUiAction() {
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
