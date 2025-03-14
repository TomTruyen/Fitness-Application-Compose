package com.tomtruyen.core.common.extensions

fun Double.tryIntString(): String {
    val isInt = this % 1 == 0.0

    return if (isInt) this.toInt().toString() else this.rounded().toString()
}

fun Double.rounded(decimals: Int = 2): String {
    return "%.${decimals}f".format(this)
}