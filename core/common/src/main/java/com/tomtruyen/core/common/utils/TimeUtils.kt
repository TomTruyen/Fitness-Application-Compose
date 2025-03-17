package com.tomtruyen.core.common.utils

import java.util.concurrent.TimeUnit

object TimeUtils {
    private const val LEADING_ZERO_WIDTH = 2
    private const val DEFAULT_DIVIDER = ":"

    fun formatSeconds(
        seconds: Long,
        alwaysShow: List<TimeUnit> = listOf(TimeUnit.MINUTES, TimeUnit.SECONDS),
        leadingZero: Boolean = true
    ): String {
        val hours = TimeUnit.SECONDS.toHours(seconds)
        val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
        val secs = seconds % 60

        return buildString {
            if (hours > 0 || alwaysShow.contains(TimeUnit.HOURS)) {
                append(padWithLeadingZeros(hours, leadingZero)).append(DEFAULT_DIVIDER)
            }
            if (minutes > 0 || alwaysShow.contains(TimeUnit.MINUTES)) {
                append(padWithLeadingZeros(minutes, leadingZero)).append(DEFAULT_DIVIDER)
            }
            if (secs > 0 || alwaysShow.contains(TimeUnit.SECONDS)) {
                append(padWithLeadingZeros(secs, leadingZero))
            }
        }
    }

    private fun padWithLeadingZeros(number: Long, leadingZero: Boolean): String {
        return if (leadingZero) number.toString()
            .padStart(LEADING_ZERO_WIDTH, '0') else number.toString()
    }

    fun formatDuration(duration: Long): String {
        val hours = TimeUnit.SECONDS.toHours(duration)
        val minutes = TimeUnit.SECONDS.toMinutes(duration) % 60
        val seconds = duration % 60

        return when {
            hours > 0 -> "${hours}h ${minutes}min"
            minutes > 0 -> "${minutes}min"
            else -> "${seconds}sec"
        }
    }
}