package com.tomtruyen.core.designsystem.theme.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ThemePreferencesDatastore: KoinComponent {
    private const val DATASTORE_NAME = "theme_preferences"
    private const val KEY_THEME = "theme"

    private val context by inject<Context>()

    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

    private val theme = stringPreferencesKey(KEY_THEME)

    enum class Mode(val value: String) {
        DARK("Dark"),
        LIGHT("Light"),
        SYSTEM("System");

        companion object {
            fun fromValue(value: String?) = Mode.entries.find { it.value == value }
        }
    }

    suspend fun setTheme(mode: Mode) {
        context.dataStore.edit {
            it[theme] = mode.value
        }
    }

    suspend fun clear() {
        context.dataStore.edit {
            it.clear()
        }
    }

    val themeMode = context.dataStore.data.map {
        Mode.fromValue(it[theme]) ?: Mode.SYSTEM
    }
}