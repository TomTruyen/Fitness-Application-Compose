package com.tomtruyen.feature.profile

import com.tomtruyen.core.common.models.UnitType

sealed class ProfileUiAction {
    data class OnUnitChanged(val value: UnitType) : ProfileUiAction()

    data class OnRestChanged(val value: Int) : ProfileUiAction()

    data class OnRestEnabledChanged(val value: Boolean) : ProfileUiAction()

    data class OnRestVibrationEnabledChanged(val value: Boolean) : ProfileUiAction()

    data object Refresh : ProfileUiAction()

    data object Logout : ProfileUiAction()
}
