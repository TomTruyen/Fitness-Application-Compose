package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.FilterOption
import com.tomtruyen.data.entities.Equipment
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class EquipmentUiModel(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String
): FilterOption {
    fun toEntity() = Equipment(
        id = id,
        name = name
    )

    companion object {
        fun fromEntity(entity: Equipment) = EquipmentUiModel(
            id = entity.id,
            name = entity.name
        )

        val DEFAULT = EquipmentUiModel(
            name = "None"
        )
    }
}