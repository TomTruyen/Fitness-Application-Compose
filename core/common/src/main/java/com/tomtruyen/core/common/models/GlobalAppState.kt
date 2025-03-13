package com.tomtruyen.core.common.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.koin.core.component.KoinComponent

object GlobalAppState: KoinComponent {
    val isBottomBarVisible: MutableState<Boolean> = mutableStateOf(false)
}
