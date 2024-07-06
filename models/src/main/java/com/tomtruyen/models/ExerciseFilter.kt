package com.tomtruyen.models

data class ExerciseFilter(
    var categories: List<String> = emptyList(),
    var equipment: List<String> = emptyList(),
) {
    fun tryAddCategory(category: String) {
        categories = categories.toMutableList().apply {
            if (contains(category)) {
                remove(category)
            } else {
                add(category)
            }
        }
    }

    fun tryAddEquipment(equipment: String) {
        this.equipment = this.equipment.toMutableList().apply {
            if (contains(equipment)) {
                remove(equipment)
            } else {
                add(equipment)
            }
        }
    }
}
