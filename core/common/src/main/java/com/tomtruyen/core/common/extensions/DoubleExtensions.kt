package com.tomtruyen.core.common.extensions

import java.util.Locale

fun Double.tryIntString(): String {
    val isInt = this % 1 == 0.0

    return if (isInt) this.toInt().toString() else this.toString()
}