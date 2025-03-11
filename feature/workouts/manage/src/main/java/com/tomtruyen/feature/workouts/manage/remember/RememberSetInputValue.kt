package com.tomtruyen.feature.workouts.manage.remember

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tomtruyen.core.common.extensions.tryIntString
import com.tomtruyen.core.common.models.ManageWorkoutMode

@Composable
fun <T> rememberSetInputValue(
    currentValue: T?,
    hasBeenCompleted: Boolean,
    mode: ManageWorkoutMode,
    transform: (T?) -> String = {
        when (it) {
            is Double? -> it?.tryIntString()
            else -> it.toString()
        }.orEmpty()
    }
): Pair<MutableState<String>, T?> {
    val initial = remember { currentValue }

    // Used to fix issue where initial == currentValue due to user input causing it to result in empty string
    var initialRender by remember { mutableStateOf(true) }

    val value = remember(mode, currentValue, hasBeenCompleted) {
        val evaluation = (!mode.isExecute || initial != currentValue || hasBeenCompleted)

        val transformedValue = if (evaluation || !initialRender) {
            transform(currentValue)
        } else ""

        initialRender = false

        mutableStateOf(transformedValue)
    }

    return value to initial
}