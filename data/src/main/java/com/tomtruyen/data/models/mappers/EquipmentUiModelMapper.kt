package com.tomtruyen.data.models.mappers

import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.models.ui.EquipmentUiModel

object EquipmentUiModelMapper {
    fun toEntity(model: EquipmentUiModel): Equipment = with(model) {
        Equipment(
            id = id,
            name = name
        )
    }

    fun fromEntity(entity: Equipment): EquipmentUiModel = with(entity) {
        EquipmentUiModel(
            id = id,
            name = name
        )
    }
}