package com.tomtruyen.core.common.models

enum class UnitType(val value: String) {
    KG("kg"),
    LBS("lbs");

    companion object {
        fun fromValue(value: String) = entries.firstOrNull {
            it.value == value
        } ?: KG
    }
}