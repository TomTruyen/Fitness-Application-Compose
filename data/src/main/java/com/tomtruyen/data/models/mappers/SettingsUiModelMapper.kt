package com.tomtruyen.data.models.mappers

import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.data.entities.Settings
import com.tomtruyen.data.models.ui.SettingsUiModel

object SettingsUiModelMapper {
    fun toEntity(model: SettingsUiModel): Settings = with(model) {
        Settings(
            id = id,
            unit = unit.value,
            rest = rest,
            restEnabled = restEnabled,
            restVibrationEnabled = restVibrationEnabled,
            synced = false
        )
    }

    fun fromEntity(entity: Settings): SettingsUiModel = with(entity) {
        SettingsUiModel(
            id = id,
            unit = UnitType.fromValue(unit),
            rest = rest,
            restEnabled = restEnabled,
            restVibrationEnabled = restVibrationEnabled
        )
    }
}