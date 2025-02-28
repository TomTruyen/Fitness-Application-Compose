package com.tomtruyen.data.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.core.common.models.ExerciseType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Parcelize
@Entity(
    tableName = Exercise.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Equipment::class,
            parentColumns = ["id"],
            childColumns = ["equipmentId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        androidx.room.Index(value = ["equipmentId"]),
        androidx.room.Index(value = ["categoryId"])
    ]
)
data class Exercise(
    @PrimaryKey
    @SerialName("id")
    override val id: String = UUID.randomUUID().toString(),
    @SerialName("name")
    val name: String? = null,
    @SerialName("category_id")
    val categoryId: String? = null,
    @SerialName("equipment_id")
    val equipmentId: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("image_detail_url")
    val imageDetailUrl: String? = null,
    @SerialName("image_thumbnail_url")
    val type: String = ExerciseType.WEIGHT.value,
    @SerialName("steps")
    val steps: List<String>? = emptyList(),
    @SerialName("user_id")
    val userId: String? = null
) : BaseEntity, Parcelable {
    // TODO: See if we can get rid of this by using the enum directly
    val typeEnum
        get() = ExerciseType.entries.firstOrNull { it.value.lowercase() == type }
            ?: ExerciseType.WEIGHT

    companion object {
        const val TABLE_NAME = "Exercise"
    }
}

data class ExerciseWithCategoryAndEquipment(
    @Embedded
    val exercise: Exercise = Exercise(),
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id",
        entity = Category::class
    )
    val category: Category? = null,
    @Relation(
        parentColumn = "equipmentId",
        entityColumn = "id",
        entity = Equipment::class
    )
    val equipment: Equipment? = null
) {
    val displayName
        get() = buildString {
            append(exercise.name)

            val equipmentName = equipment?.name.orEmpty()
            if (equipmentName.isNotBlank()) {
                append(" ($equipmentName)")
            }
        }
}