package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.data.entities.Settings
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
) {
    fun toEntity() = Settings(
        id = id,
        unit = unit.value,
        rest = rest,
        restEnabled = restEnabled,
        restVibrationEnabled = restVibrationEnabled
    )

    companion object {
        fun fromEntity(entity: Settings) = SettingsUiModel(
            id = entity.id,
            unit = UnitType.fromValue(entity.unit),
            rest = entity.rest,
            restEnabled = entity.restEnabled,
            restVibrationEnabled = entity.restVibrationEnabled
        )
    }
}