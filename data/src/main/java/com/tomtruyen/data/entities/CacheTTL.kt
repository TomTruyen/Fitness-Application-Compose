package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// This class is used to manage the cache time-to-live of Firebase data to reduce reads
@Entity(tableName = CacheTTL.TABLE_NAME)
data class CacheTTL(
    @PrimaryKey override val id: String, // The key = the Firebase collection name
    val ttl: Long = DEFAULT_TTL
) : BaseEntity {
    val isExpired: Boolean
        get() = System.currentTimeMillis() > ttl

    companion object {
        const val TABLE_NAME = "cache_ttl"

        val DEFAULT_TTL = System.currentTimeMillis() + 60 * 60 * 1000L // 1 hour
    }
}