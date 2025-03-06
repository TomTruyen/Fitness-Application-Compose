package com.tomtruyen.data.models.ui

import androidx.compose.runtime.Immutable
import com.tomtruyen.core.common.models.FilterOption
import com.tomtruyen.data.entities.Category
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Immutable
data class CategoryUiModel(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String
) : FilterOption {
    fun toEntity() = Category(
        id = id,
        name = name
    )

    companion object {
        fun fromEntity(entity: Category) = CategoryUiModel(
            id = entity.id,
            name = entity.name
        )

        val DEFAULT = CategoryUiModel(
            name = "None"
        )
    }
}