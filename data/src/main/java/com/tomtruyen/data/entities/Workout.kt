package com.tomtruyen.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.tomtruyen.core.common.models.UnitType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = Workout.TABLE_NAME)
data class Workout(
    @PrimaryKey
    @SerialName("id")
    override val id: String = UUID.randomUUID().toString(),
    @SerialName("name")
    val name: String = "",
    @SerialName("unit")
    val unit: String = UnitType.KG.value, // KG or LBS
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @SerialName("user_id")
    val userId: String? = null,
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "Workout"
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
