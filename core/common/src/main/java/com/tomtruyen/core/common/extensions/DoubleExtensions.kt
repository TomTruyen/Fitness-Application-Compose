package com.tomtruyen.core.common.extensions

import java.util.Locale

fun Double.format() = if (this % 1 != 0.0) {
    String.format(Locale.getDefault(), "%.0f", this)
} else {
    String.format(Locale.getDefault(), "%.1f", this)
}

fun Double.tryIntString(): String {
    val isInt = this % 1 == 0.0

    return if (isInt) this.toInt().toString() else this.toString()
}