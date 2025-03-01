package com.tomtruyen.data.models.network

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.entities.Exercise
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseNetworkModel(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String?,
    @SerialName("category_id")
    val categoryId: String?,
    @SerialName("equipment_id")
    val equipmentId: String?,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("image_detail_url")
    val imageDetailUrl: String?,
    @SerialName("type")
    val type: String,
    @SerialName("steps")
    val steps: List<String>?,
    @SerialName("user_id")
    val userId: String?,
    @SerialName("Category")
    val category: Category?,
    @SerialName("Equipment")
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