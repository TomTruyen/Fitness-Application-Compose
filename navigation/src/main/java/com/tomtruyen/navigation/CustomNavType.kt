package com.tomtruyen.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel
import kotlinx.serialization.json.Json

object CustomNavType {
    val WorkoutType = createNavType<WorkoutUiModel>(isNullableAllowed = true)
    val WorkoutExerciseType = createNavType<WorkoutExerciseUiModel>(isNullableAllowed = false)
}

inline fun <reified T> createNavType(isNullableAllowed: Boolean = false): NavType<T> {
    return object : NavType<T>(isNullableAllowed) {
        override fun get(bundle: Bundle, key: String): T? {
            return bundle.getString(key)?.let { Json.decodeFromString(it) }
        }

        override fun parseValue(value: String): T {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: T): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}