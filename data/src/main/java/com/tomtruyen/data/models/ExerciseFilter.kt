package com.tomtruyen.data.models

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment

data class ExerciseFilter(
    var categories: List<Category> = emptyList(),
    var equipment: List<Equipment> = emptyList(),
) {
    fun tryAddCategory(category: Category) {
        this.categories = this.categories.toMutableList().apply {
            if (contains(category)) {
                remove(category)
            } else {
                add(category)
            }
        }
    }

    fun tryAddEquipment(equipment: Equipment) {
        this.equipment = this.equipment.toMutableList().apply {
            if (contains(equipment)) {
                remove(equipment)
            } else {
                add(equipment)
            }
        }
    }
}
