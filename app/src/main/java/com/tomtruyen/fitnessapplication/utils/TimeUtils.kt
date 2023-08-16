package com.tomtruyen.fitnessapplication.utils

object TimeUtils {
    fun formatSeconds(seconds: Int): String {
        if (seconds >= 60) {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60

            return if (remainingSeconds > 0) {
                if (minutes == 1) {
                    "$minutes" + "m " + "$remainingSeconds" + "s"
                } else {
                    "$minutes" + "m " + "$remainingSeconds" + "s"
                }
            } else {
                "$minutes" + "m"
            }
        } else {
            return "$seconds" + "s"
        }
    }
}