package com.tomtruyen.feature.workouts.manage.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tomtruyen.core.common.models.ManageWorkoutMode

@Composable
fun <T, R> rememberSetInputValue(
    value: T?,
    mode: ManageWorkoutMode,
    didChange: () -> Boolean,
    defaultValue: R,
    transform: (T?) -> R
): Pair<MutableState<R>, T?> {
    val initial = remember { value }

    val value = remember(value, mode, didChange) {
        mutableStateOf(
            if(!mode.isExecute || didChange()) {
                transform(value)
            } else {
                defaultValue
            }
        )
    }

    return value to initial
}