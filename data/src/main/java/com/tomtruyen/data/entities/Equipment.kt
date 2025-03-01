package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = Equipment.TABLE_NAME)
data class Equipment(
    @PrimaryKey
    @SerialName("id")
    override val id: String = UUID.randomUUID().toString(),
    @SerialName("name")
    val name: String,
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "Equipment"
    }
}