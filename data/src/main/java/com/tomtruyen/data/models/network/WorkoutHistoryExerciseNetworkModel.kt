package com.tomtruyen.data.models.network

import com.tomtruyen.data.entities.Exercise
import com.tomtruyen.data.entities.WorkoutHistoryExercise
import com.tomtruyen.data.entities.WorkoutHistoryExerciseSet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutHistoryExerciseNetworkModel(
    @SerialName(WorkoutHistoryExercise.KEY_ID)
    val id: String,
    @SerialName(WorkoutHistoryExercise.KEY_NAME)
    val name: String,
    @SerialName(WorkoutHistoryExercise.KEY_IMAGE_URL)
    val imageUrl: String?,
    @SerialName(WorkoutHistoryExercise.KEY_TYPE)
    val type: String,
    @SerialName(WorkoutHistoryExercise.KEY_NOTES)
    val notes: String?,
    @SerialName(WorkoutHistoryExercise.KEY_SORT_ORDER)
    val sortOrder: Int,
    @SerialName(WorkoutHistoryExercise.KEY_EXERCISE_ID)
    val exerciseId: String,
    @SerialName(WorkoutHistoryExercise.KEY_WORKOUT_HISTORY_ID)
    val workoutHistoryId: String,
    @SerialName(WorkoutHistoryExercise.KEY_CATEGORY)
    val category: String?,
    @SerialName(WorkoutHistoryExercise.KEY_EQUIPMENT)
    val equipment: String?,
    @SerialName(Exercise.TABLE_NAME)
    val exercise: ExerciseNetworkModel?,
    @SerialName(WorkoutHistoryExerciseSet.TABLE_NAME)
    val sets: List<WorkoutHistoryExerciseSet>
) {
    fun toEntity() = WorkoutHistoryExercise(
        id = id,
        name = name,
        imageUrl = imageUrl,
        type = type,
        notes = notes,
        sortOrder = sortOrder,
        exerciseId = exerciseId,
        workoutHistoryId = workoutHistoryId,
        category = category,
        equipment = equipment,
    )
}
