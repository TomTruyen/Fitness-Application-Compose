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
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerialName("name")
    val name: String,
) {
    companion object {
        const val TABLE_NAME = "Category"
    }
}