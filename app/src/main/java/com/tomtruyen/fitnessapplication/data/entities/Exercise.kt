package com.tomtruyen.fitnessapplication.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = Exercise.TABLE_NAME)
data class Exercise(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val equipment: String,
    val image: String?,
    @SerializedName("image_detail")
    val imageDetail: String?,
    val type: String
) {
    enum class Type {
        @SerializedName("weight")
        WEIGHT,
        @SerializedName("time")
        TIME
    }
    companion object {
        const val TABLE_NAME = "exercises"
    }
}