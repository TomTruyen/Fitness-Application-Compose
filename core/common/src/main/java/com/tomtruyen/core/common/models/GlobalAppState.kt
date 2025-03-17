package com.tomtruyen.core.common.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.tomtruyen.core.common.ThemeMode
import com.tomtruyen.core.common.ThemePreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object GlobalAppState : KoinComponent {
    private val themePreferences by inject<ThemePreferences>()

    val isBottomBarVisible: MutableState<Boolean> = mutableStateOf(false)
    val theme: MutableState<ThemeMode> = mutableStateOf(themePreferences.getTheme())
}
