package com.tomtruyen.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object Global {
    val isBottomBarVisible: MutableState<Boolean> = mutableStateOf(false)
}