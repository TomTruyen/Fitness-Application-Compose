package com.tomtruyen.core.common.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object GlobalAppState {
    val isBottomBarVisible: MutableState<Boolean> = mutableStateOf(false)
}