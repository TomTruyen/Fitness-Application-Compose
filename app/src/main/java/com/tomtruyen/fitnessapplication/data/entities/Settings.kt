package com.tomtruyen.fitnessapplication.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings(
    @PrimaryKey var id: String = "", // Firebase User ID
    var unit: String = UnitType.KG.value,
    var rest: Int = 30,
    var restEnabled: Boolean = true,
    var restVibrationEnabled: Boolean = true,
) {
    enum class UnitType(val value: String) {
        KG("kg"),
        LBS("lbs")
    }

    companion object {
        const val TABLE_NAME = "settings"
    }
}