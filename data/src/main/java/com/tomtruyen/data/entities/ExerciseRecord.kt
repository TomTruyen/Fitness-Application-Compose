package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tomtruyen.core.common.utils.CacheKeyHelper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = ExerciseRecord.TABLE_NAME
)
data class ExerciseRecord(
    @PrimaryKey
    @SerialName(KEY_EXERCISE_ID)
    val exerciseId: String,
    @SerialName(KEY_REPS)
    val reps: Int? = null,
    @SerialName(KEY_WEIGHT)
    val weight: Double? = null,
    @SerialName(KEY_TIME)
    val time: Int? = null
) {
    companion object: CacheKeyHelper(ExerciseRecord.TABLE_NAME) {
        const val KEY_EXERCISE_ID = "exercise_id"
        const val KEY_REPS = "reps"
        const val KEY_WEIGHT = "weight"
        const val KEY_TIME = "time"

        const val RPC_FUNCTION = "get_user_latest_records_for_exercises"
        const val EXERCISE_ID_PARAM = "exercise_ids"

        const val TABLE_NAME = "ExerciseRecord"
    }
}