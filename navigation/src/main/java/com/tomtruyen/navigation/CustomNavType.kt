package com.tomtruyen.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.tomtruyen.data.models.ui.WorkoutUiModel
import kotlinx.serialization.json.Json

object CustomNavType {
    val WorkoutType = object: NavType<WorkoutUiModel>(
        isNullableAllowed = true
    ) {
        override fun get(
            bundle: Bundle,
            key: String
        ): WorkoutUiModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): WorkoutUiModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: WorkoutUiModel): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: WorkoutUiModel
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}