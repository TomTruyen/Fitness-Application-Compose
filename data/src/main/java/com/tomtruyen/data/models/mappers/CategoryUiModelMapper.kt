package com.tomtruyen.data.models.mappers

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.models.ui.CategoryUiModel

object CategoryUiModelMapper {
    fun toEntity(model: CategoryUiModel): Category = with(model) {
        Category(
            id = id,
            name = name
        )
    }

    fun fromEntity(entity: Category): CategoryUiModel = with(entity) {
        CategoryUiModel(
            id = id,
            name = name
        )
    }
}