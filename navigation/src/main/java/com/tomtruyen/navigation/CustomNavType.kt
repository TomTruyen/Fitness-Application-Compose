package com.tomtruyen.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.tomtruyen.core.common.JsonInstance
import com.tomtruyen.data.models.ui.WorkoutExerciseUiModel
import com.tomtruyen.data.models.ui.WorkoutUiModel

object CustomNavType {
    val WorkoutType = createNavType<WorkoutUiModel>(isNullableAllowed = true)
    val WorkoutExerciseListType = createNavType<List<WorkoutExerciseUiModel>>(isNullableAllowed = false)
}

inline fun <reified T> createNavType(isNullableAllowed: Boolean = false): NavType<T> {
    return object : NavType<T>(isNullableAllowed) {
        override fun get(bundle: Bundle, key: String): T? {
            return bundle.getString(key)?.let { JsonInstance.decodeFromString(it) }
        }

        override fun parseValue(value: String): T {
            return JsonInstance.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: T): String {
            return Uri.encode(JsonInstance.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putString(key, JsonInstance.encodeToString(value))
        }
    }
}