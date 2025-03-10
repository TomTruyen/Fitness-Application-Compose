package com.tomtruyen.core.common.utils

import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class StopwatchTimer(
    initialTimeInSeconds: Long = 0L,
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val isActive = AtomicBoolean(false)

    // Time in Seconds
    private val _time = MutableStateFlow(initialTimeInSeconds)
    val time = _time.asStateFlow()

    private var lastUpdateTime: Long = 0L

    fun start() {
        if (isActive.get()) return

        lastUpdateTime = SystemClock.elapsedRealtime()

        scope.launch {
            this@StopwatchTimer.isActive.set(true)
            while (this@StopwatchTimer.isActive.get()) {
                delay(DELAY_IN_MILLIS)

                val currentTime = SystemClock.elapsedRealtime()
                val elapsedTime = (currentTime - lastUpdateTime) / 1000
                lastUpdateTime = currentTime

                _time.update {
                    it + elapsedTime
                }
            }
        }
    }

    @Synchronized
    fun stop() {
        isActive.set(false)
    }

    companion object {
        private const val DELAY_IN_MILLIS = 1000L
    }
}