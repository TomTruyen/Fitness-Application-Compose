package com.tomtruyen.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Parcelize
@Entity(tableName = Exercise.TABLE_NAME)
data class Exercise(
    @PrimaryKey
    @SerialName("id")
    override val id: String = UUID.randomUUID().toString(),
    @SerialName("name")
    val name: String? = null,
    val category: String? = DEFAULT_DROPDOWN_VALUE,
    val equipment: String? = DEFAULT_DROPDOWN_VALUE,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("image_detail_url")
    val imageDetailUrl: String? = null,
    @SerialName("image_thumbnail_url")
    val type: String = ExerciseType.WEIGHT.value,
    @SerialName("steps")
    val steps: List<String> = emptyList(),
    @SerialName("user_id")
    val userId: String? = null
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
        const val TABLE_NAME = "Exercise"
        const val DEFAULT_DROPDOWN_VALUE = "None"
    }
}