package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tomtruyen.core.common.models.FilterOption
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = Category.TABLE_NAME)
data class Category(
    @PrimaryKey
    @SerialName("id")
    override val id: String = UUID.randomUUID().toString(),
    @SerialName("name")
    override val name: String,
): BaseEntity, FilterOption {
    val isDefault: Boolean
        get() = this == DEFAULT

    companion object {
        const val TABLE_NAME = "Category"

        val DEFAULT = Category(
            name = "None"
        )
    }
}