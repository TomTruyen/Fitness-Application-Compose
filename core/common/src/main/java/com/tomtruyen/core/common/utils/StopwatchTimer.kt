package com.tomtruyen.core.common.utils

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
    private var scope = CoroutineScope(Dispatchers.IO)

    private val isActive = AtomicBoolean(false)

    private val _time = MutableStateFlow(initialTimeInSeconds)
    val time = _time.asStateFlow()

    fun start() {
        if(isActive.get()) return

        scope.launch {
            this@StopwatchTimer.isActive.set(true)
            while(this@StopwatchTimer.isActive.get()) {
                delay(DELAY_IN_MILLIS)

                _time.update {
                    it + (DELAY_IN_MILLIS / 1000)
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