package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = Category.TABLE_NAME)
data class Category(
    @PrimaryKey
    @SerialName(KEY_ID)
    override val id: String = UUID.randomUUID().toString(),
    @SerialName(KEY_NAME)
    val name: String,
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "Category"

        const val KEY_ID = "id"
        const val KEY_NAME = "name"
    }
}