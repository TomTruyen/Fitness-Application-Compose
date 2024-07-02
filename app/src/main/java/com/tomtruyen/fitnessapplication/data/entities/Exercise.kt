package com.tomtruyen.fitnessapplication.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.encoders.annotations.Encodable.Ignore
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = Exercise.TABLE_NAME)
data class Exercise(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
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
): Parcelable {
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