package com.tomtruyen.data.models

import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.models.ui.EquipmentUiModel

data class ExerciseFilter(
    var categories: List<CategoryUiModel> = emptyList(),
    var equipment: List<EquipmentUiModel> = emptyList(),
) {
    fun tryAddCategory(category: CategoryUiModel) {
        this.categories = this.categories.toMutableList().apply {
            if (contains(category)) {
                remove(category)
            } else {
                add(category)
            }
        }
    }

    fun tryAddEquipment(equipment: EquipmentUiModel) {
        this.equipment = this.equipment.toMutableList().apply {
            if (contains(equipment)) {
                remove(equipment)
            } else {
                add(equipment)
            }
        }
    }
}
