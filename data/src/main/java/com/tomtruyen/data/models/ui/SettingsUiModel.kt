package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.UnitType
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class SettingsUiModel(
    val id: String = UUID.randomUUID().toString(),
    val unit: UnitType = UnitType.KG,
    val rest: Int = 30,
    val restEnabled: Boolean = true,
    val restVibrationEnabled: Boolean = true
)