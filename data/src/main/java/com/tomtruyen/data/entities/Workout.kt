package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.core.common.models.UnitType
import com.tomtruyen.core.common.serializer.SupabaseDateTimeSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = Workout.TABLE_NAME)
data class Workout(
    @PrimaryKey
    @SerialName(KEY_ID)
    override val id: String = UUID.randomUUID().toString(),
    @SerialName(KEY_NAME)
    val name: String = "",
    @SerialName(KEY_UNIT)
    val unit: String = UnitType.KG.value, // KG or LBS
    @SerialName(KEY_CREATED_AT)
    @Serializable(with = SupabaseDateTimeSerializer::class)
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    @SerialName(KEY_USER_ID)
    val userId: String? = null,
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "Workout"

        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_UNIT = "unit"
        const val KEY_CREATED_AT = "created_at"
        const val KEY_USER_ID = "user_id"
    }
}

data class WorkoutWithExercises(
    @Embedded
    val workout: Workout = Workout(),
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId",
        entity = WorkoutExercise::class
    )
    val exercises: List<WorkoutExerciseWithSets> = emptyList()
)
