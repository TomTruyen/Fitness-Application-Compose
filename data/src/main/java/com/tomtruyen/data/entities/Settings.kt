package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = Settings.TABLE_NAME)
data class Settings(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    val unit: String = UnitType.KG.value,
    val rest: Int = 30,
    val restEnabled: Boolean = true,
    val restVibrationEnabled: Boolean = true,

): BaseEntity {
    enum class UnitType(val value: String) {
        KG("kg"),
        LBS("lbs")
    }

    companion object {
        const val TABLE_NAME = "settings"
    }
}