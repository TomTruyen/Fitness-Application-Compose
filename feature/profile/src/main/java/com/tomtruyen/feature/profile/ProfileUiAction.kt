package com.tomtruyen.feature.profile

import com.tomtruyen.core.common.models.UnitType

sealed class ProfileUiAction {
    data class UnitChanged(val value: UnitType) : ProfileUiAction()

    data class RestChanged(val value: Int) : ProfileUiAction()

    data class RestEnabledChanged(val value: Boolean) : ProfileUiAction()

    data class RestVibrationEnabledChanged(val value: Boolean) : ProfileUiAction()

    data object OnRefresh : ProfileUiAction()

    data object Logout : ProfileUiAction()
}
