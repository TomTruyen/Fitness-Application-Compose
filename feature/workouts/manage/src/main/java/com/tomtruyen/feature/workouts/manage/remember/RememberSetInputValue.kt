package com.tomtruyen.feature.workouts.manage.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tomtruyen.core.common.extensions.tryIntString
import com.tomtruyen.core.common.models.ManageWorkoutMode

@Composable
fun <T> rememberSetInputValue(
    currentValue: T?,
    mode: ManageWorkoutMode,
    wasChanged: () -> Boolean,
    transform: (T?) -> String = {
        when (it) {
            is Double? -> it?.tryIntString()
            else -> it.toString()
        }.orEmpty()
    }
): Pair<MutableState<String>, T?> {
    val initial = remember { currentValue }

    val value = remember(currentValue, mode, wasChanged) {
        mutableStateOf(
            if(!mode.isExecute || wasChanged()) {
                transform(currentValue)
            } else {
                ""
            }
        )
    }

    return value to initial
}