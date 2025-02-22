package com.tomtruyen.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.encoders.annotations.Encodable.Ignore
import com.google.firebase.firestore.PropertyName
import com.tomtruyen.data.entities.BaseEntity.Companion.DEFAULT_TTL
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = Exercise.TABLE_NAME)
data class Exercise(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    override val ttl: Long = DEFAULT_TTL,
    val name: String? = null,
    val category: String? = null,
    val equipment: String? = null,
    val image: String? = null,
    @PropertyName("image_detail")
    val imageDetail: String? = null,
    val type: String = ExerciseType.WEIGHT.value,
    @PropertyName("description")
    val steps: List<String> = emptyList(),
    val isUserCreated: Boolean = false,
): BaseEntity, Parcelable {
    @get:Ignore
    val displayName get() = buildString {
        append(name)
        if(!equipment.isNullOrBlank()) {
            append(" ($equipment)")
        }
    }

    @get:Ignore
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