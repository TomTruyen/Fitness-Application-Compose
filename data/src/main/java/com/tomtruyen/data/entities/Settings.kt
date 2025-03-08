package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tomtruyen.core.common.models.UnitType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
@Entity(tableName = Settings.TABLE_NAME)
data class Settings(
    @PrimaryKey
    @SerialName(KEY_ID)
    override val id: String = UUID.randomUUID().toString(),
    @SerialName(KEY_UNIT)
    val unit: String = UnitType.KG.value,
    @SerialName(KEY_REST)
    val rest: Int = 30,
    @SerialName(KEY_REST_ENABLED)
    val restEnabled: Boolean = true,
    @SerialName(KEY_REST_VIBRATION_ENABLED)
    val restVibrationEnabled: Boolean = true,
    @SerialName(KEY_USER_ID)
    val userId: String? = null,
    @Transient
    override val synced: Boolean = true,
) : BaseEntity, SyncEntity {
    companion object {
        const val TABLE_NAME = "Settings"

        const val KEY_ID = "id"
        const val KEY_UNIT = "unit"
        const val KEY_REST = "rest"
        const val KEY_REST_ENABLED = "rest_enabled"
        const val KEY_REST_VIBRATION_ENABLED = "rest_vibration_enabled"
        const val KEY_USER_ID = "user_id"
    }
}