package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tomtruyen.data.entities.BaseEntity.Companion.DEFAULT_TTL
import java.util.UUID

@Entity(tableName = Settings.TABLE_NAME)
data class Settings(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    override val ttl: Long = DEFAULT_TTL,
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