package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.FilterOption
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class EquipmentUiModel(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String
) : FilterOption {
    companion object {
        val DEFAULT = EquipmentUiModel(
            name = "None"
        )
    }
}