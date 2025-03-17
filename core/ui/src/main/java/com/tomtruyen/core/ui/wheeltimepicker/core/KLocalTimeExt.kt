package com.tomtruyen.core.ui.wheeltimepicker.core

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun LocalTime.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalTime {
    return Clock.System.now().toLocalDateTime(timeZone).time
}

internal val LocalTime.Companion.MIN: LocalTime get() = LocalTime(0, 0, 0, 0)
internal val LocalTime.Companion.MAX: LocalTime get() = LocalTime(23, 59, 59, 999_999_999)

internal fun LocalTime.withSecond(second: Int): LocalTime {
    return if (this.second == second) {
        this
    } else {
        LocalTime(hour, minute, second, nanosecond)
    }
}


internal fun LocalTime.withMinute(minute: Int): LocalTime {
    return if (this.minute == minute) {
        this
    } else {
        LocalTime(hour, minute, second, nanosecond)
    }
}

internal fun LocalTime.withHour(hour: Int): LocalTime {
    return if (this.hour == hour) {
        this
    } else {
        LocalTime(hour, minute, second, nanosecond)
    }
}

internal fun LocalTime.isBefore(other: LocalTime): Boolean {
    return compareTo(other) < 0
}

internal fun LocalTime.isAfter(other: LocalTime): Boolean {
    return compareTo(other) > 0
}