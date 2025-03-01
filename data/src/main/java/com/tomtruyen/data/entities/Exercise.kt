package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.core.common.models.ExerciseType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
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
    @SerialName(KEY_ID)
    override val id: String = UUID.randomUUID().toString(),
    @SerialName(KEY_NAME)
    val name: String = "",
    @SerialName(KEY_CATEGORY_ID)
    val categoryId: String? = null,
    @SerialName(KEY_EQUIPMENT_ID)
    val equipmentId: String? = null,
    @SerialName(KEY_IMAGE_URL)
    val imageUrl: String? = null,
    @SerialName(KEY_IMAGE_DETAIL_URL)
    val imageDetailUrl: String? = null,
    @SerialName(KEY_TYPE)
    val type: String = ExerciseType.WEIGHT.value,
    @SerialName(KEY_STEPS)
    val steps: List<String>? = emptyList(),
    @SerialName(KEY_USER_ID)
    val userId: String? = null
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "Exercise"

        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_EQUIPMENT_ID = "equipment_id"
        const val KEY_IMAGE_URL = "image_url"
        const val KEY_IMAGE_DETAIL_URL = "image_detail_url"
        const val KEY_TYPE = "type"
        const val KEY_STEPS = "steps"
        const val KEY_USER_ID = "user_id"
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
)