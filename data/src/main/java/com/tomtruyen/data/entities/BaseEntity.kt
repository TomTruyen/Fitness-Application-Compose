package com.tomtruyen.data.entities

interface BaseEntity {
    val id: String
    val ttl: Long

    companion object {
        val DEFAULT_TTL = System.currentTimeMillis() + 60 * 60 * 1000L // 1 hour
    }
}