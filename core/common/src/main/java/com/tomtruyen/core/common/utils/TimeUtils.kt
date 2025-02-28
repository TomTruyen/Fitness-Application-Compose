package com.tomtruyen.core.common.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtils {
    private const val LEADING_ZERO_WIDTH = 2
    private const val DEFAULT_DIVIDER = ":"

    fun formatSeconds(
        seconds: Long,
        alwaysShow: List<TimeUnit> = listOf(TimeUnit.MINUTES, TimeUnit.SECONDS),
        leadingZero: Boolean = true
    ): String {
        val builder = StringBuilder()

        val hours = TimeUnit.SECONDS.toHours(seconds)
        val minute = TimeUnit.SECONDS.toMinutes(seconds - TimeUnit.HOURS.toSeconds(hours))
        val second = TimeUnit.SECONDS.toSeconds(
            seconds - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute)
        )

        if (hours > 0 || alwaysShow.contains(TimeUnit.HOURS)) {
            builder.append(padWithLeadingZeros(hours, leadingZero))
            builder.append(DEFAULT_DIVIDER)
        }

        if (minute > 0 || alwaysShow.contains(TimeUnit.MINUTES)) {
            builder.append(padWithLeadingZeros(minute, leadingZero))
            builder.append(DEFAULT_DIVIDER)
        }

        if (second > 0 || alwaysShow.contains(TimeUnit.SECONDS)) {
            builder.append(padWithLeadingZeros(second, leadingZero))
        }

        return builder.toString()
    }

    private fun padWithLeadingZeros(number: Long, leadingZero: Boolean): String {
        val str = number.toString()

        if (!leadingZero || str.length >= LEADING_ZERO_WIDTH) return str

        return "0".repeat(LEADING_ZERO_WIDTH - str.length) + str
    }

    fun formatDate(dateMillis: Long): String = SimpleDateFormat(
        "dd MMM yyyy HH:mm",
        Locale.getDefault()
    ).format(dateMillis)
}