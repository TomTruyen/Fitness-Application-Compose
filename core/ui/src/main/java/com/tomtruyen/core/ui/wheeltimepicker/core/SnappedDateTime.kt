package com.tomtruyen.core.ui.wheeltimepicker.core

import kotlinx.datetime.LocalTime

internal sealed class SnappedDateTime(val snappedLocalTime: LocalTime, val snappedIndex: Int) {
    data class Hour(val localTime: LocalTime, val index: Int) : SnappedDateTime(localTime, index)
    data class Minute(val localTime: LocalTime, val index: Int) : SnappedDateTime(localTime, index)
    data class Second(val localTime: LocalTime, val index: Int) : SnappedDateTime(localTime, index)
}