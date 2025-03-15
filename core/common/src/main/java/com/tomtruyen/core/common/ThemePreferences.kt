package com.tomtruyen.core.common

import android.content.Context
import androidx.core.content.edit
import com.tomtruyen.core.common.models.GlobalAppState

// Convert to SharedPreferences
// Keep track of current state in the AppState
// That can then be a mutableState there, which should reduce unecassary rerenders
class ThemePreferences(
    private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "theme_preferences"
        private const val KEY_THEME = "theme"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setTheme(mode: ThemeMode) {
        sharedPreferences.edit { putString(KEY_THEME, mode.value) }.also {
            GlobalAppState.theme.value = mode
        }
    }

    fun getTheme(): ThemeMode {
        val value = sharedPreferences.getString(KEY_THEME, ThemeMode.SYSTEM.value)
        return ThemeMode.fromValue(value) ?: ThemeMode.SYSTEM
    }
}

enum class ThemeMode(val value: String) {
    DARK("Dark"),
    LIGHT("Light"),
    SYSTEM("System");

    companion object {
        fun fromValue(value: String?) = entries.find { it.value == value }
    }
}