package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = Settings.TABLE_NAME)
data class Settings(
    @PrimaryKey
    @SerialName("id")
    override val id: String = UUID.randomUUID().toString(),
    @SerialName("unit")
    val unit: String = UnitType.KG.value,
    @SerialName("rest")
    val rest: Int = 30,
    @SerialName("rest_enabled")
    val restEnabled: Boolean = true,
    @SerialName("rest_vibration_enabled")
    val restVibrationEnabled: Boolean = true,
    @SerialName("user_id")
    val userId: String? = null,

    ) : BaseEntity {
    enum class UnitType(val value: String) {
        KG("kg"),
        LBS("lbs")
    }

    companion object {
        const val TABLE_NAME = "Settings"
    }
}