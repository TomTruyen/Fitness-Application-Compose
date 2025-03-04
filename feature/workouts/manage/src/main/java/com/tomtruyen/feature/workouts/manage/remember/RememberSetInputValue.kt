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
    hasBeenCompleted: Boolean,
    mode: ManageWorkoutMode,
    transform: (T?) -> String = {
        when(it) {
            is Double? -> it?.tryIntString()
            else -> it?.toString()
        }.orEmpty()
    }
): Pair<MutableState<String>, T?> {
    val initial = remember { currentValue }

    val value = remember(mode, currentValue, hasBeenCompleted) {
        val evaluation = (!mode.isExecute || initial != currentValue || hasBeenCompleted)

        val transformedValue = if(evaluation)  {
            transform(currentValue)
        } else ""
        mutableStateOf(transformedValue)
    }

    return value to initial
}