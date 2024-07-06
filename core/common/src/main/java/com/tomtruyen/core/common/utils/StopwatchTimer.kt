package com.tomtruyen.core.common.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class StopwatchTimer(
    private val initialTimeInSeconds: Long = 0L,
) {
    private var scope = CoroutineScope(Dispatchers.Main)
    var currentTime = initialTimeInSeconds
        private set

    private var isActive = false

    var time by mutableStateOf(formatTime())

    fun start() {
        if(isActive) return

        scope.launch {
            this@StopwatchTimer.isActive = true
            while(this@StopwatchTimer.isActive) {
                delay(DELAY_IN_MILLIS)
                currentTime += DELAY_IN_MILLIS / 1000
                time = formatTime()
            }
        }
    }

    @Synchronized
    fun stop() {
        isActive = false
    }

    fun reset() {
        scope.cancel()
        scope = CoroutineScope(Dispatchers.Main)
        currentTime = initialTimeInSeconds
        time = formatTime()
        isActive = false
    }

    private fun formatTime() = TimeUtils.formatSeconds(
        seconds = currentTime,
        alwaysShow = listOf(TimeUnit.HOURS , TimeUnit.MINUTES, TimeUnit.SECONDS),
    )

    companion object {
        private const val DELAY_IN_MILLIS = 1000L
    }
}