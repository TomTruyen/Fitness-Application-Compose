package com.tomtruyen.core.common.extensions

fun Double.tryIntString(): String {
    val isInt = this % 1 == 0.0

    return if (isInt) this.toInt().toString() else this.toString()
}

fun Double.rounded(decimals: Int = 2): String {
    val isInt = this % 1 == 0.0

    if (isInt) return this.toInt().toString()

    return "%.${decimals}f".format(this).trimEnd('0').trimEnd('.')
}