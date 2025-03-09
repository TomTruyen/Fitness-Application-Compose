package com.tomtruyen.core.common.models

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object GlobalAppState: KoinComponent {
    val isBottomBarVisible: MutableState<Boolean> = mutableStateOf(false)
}
