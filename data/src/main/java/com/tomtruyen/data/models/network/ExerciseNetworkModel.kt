package com.tomtruyen.data.models.network

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseNetworkModel(
    @SerialName(Exercise.KEY_ID)
    val id: String,
    @SerialName(Exercise.KEY_NAME)
    val name: String,
    @SerialName(Exercise.KEY_CATEGORY_ID)
    val categoryId: String?,
    @SerialName(Exercise.KEY_EQUIPMENT_ID)
    val equipmentId: String?,
    @SerialName(Exercise.KEY_IMAGE_URL)
    val imageUrl: String?,
    @SerialName(Exercise.KEY_IMAGE_DETAIL_URL)
    val imageDetailUrl: String?,
    @SerialName(Exercise.KEY_TYPE)
    val type: String,
    @SerialName(Exercise.KEY_STEPS)
    val steps: List<String>?,
    @SerialName(Exercise.KEY_USER_ID)
    val userId: String?,
    @SerialName(Category.TABLE_NAME)
    val category: Category?,
    @SerialName(Equipment.TABLE_NAME)
    val equipment: Equipment?
) {
    fun toEntity() = Exercise(
        id = id,
        name = name,
        categoryId = categoryId,
        equipmentId = equipmentId,
        imageUrl = imageUrl,
        imageDetailUrl = imageDetailUrl,
        type = type,
        steps = steps,
        userId = userId
    )
}