package com.tomtruyen.fitnessapplication.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = Exercise.TABLE_NAME)
data class Exercise(
    @PrimaryKey var id: String = "",
    var name: String? = null,
    var category: String? = null,
    var equipment: String? = null,
    var image: String? = null,
    @get:PropertyName("image_detail")
    @set:PropertyName("image_detail")
    var imageDetail: String? = null,
    var type: String = ExerciseType.WEIGHT.value,
    @get:PropertyName("description")
    @set:PropertyName("description")
    var steps: List<String> = emptyList(),
    var isUserCreated: Boolean = false
) {
    val displayName get() = buildString {
        append(name)
        if(!equipment.isNullOrBlank()) {
            append(" ($equipment)")
        }
    }

    val typeEnum get() = ExerciseType.values().firstOrNull { it.value == type } ?: ExerciseType.WEIGHT

    enum class ExerciseType(val value: String) {
        WEIGHT("weight"),
        TIME("time")
    }

    companion object {
        const val TABLE_NAME = "exercises"
    }
}