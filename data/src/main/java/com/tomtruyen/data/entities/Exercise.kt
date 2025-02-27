package com.tomtruyen.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = Exercise.TABLE_NAME)
data class Exercise(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    val name: String? = null,
    val category: String? = DEFAULT_DROPDOWN_VALUE,
    val equipment: String? = DEFAULT_DROPDOWN_VALUE,
    val imageUrl: String? = null,
    val imageDetailUrl: String? = null,
    val type: String = ExerciseType.WEIGHT.value,
    val steps: List<String> = emptyList(),
    val isUserCreated: Boolean = false,
): BaseEntity, Parcelable {
    val displayName get() = buildString {
        append(name)
        if(!equipment.isNullOrBlank()) {
            append(" ($equipment)")
        }
    }

    val typeEnum get() = ExerciseType.entries.firstOrNull { it.value.lowercase() == type } ?: ExerciseType.WEIGHT

    enum class ExerciseType(val value: String) {
        WEIGHT("Weight"),
        TIME("Time")
    }

    companion object {
        const val TABLE_NAME = "exercises"
        const val DEFAULT_DROPDOWN_VALUE = "None"
    }
}