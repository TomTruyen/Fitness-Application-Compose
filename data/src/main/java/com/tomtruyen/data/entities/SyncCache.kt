package com.tomtruyen.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// This class is used to manage the cache time-to-live of Supabase data to reduce reads
@Entity(tableName = SyncCache.TABLE_NAME)
data class SyncCache(
    @PrimaryKey override val id: String, // The key = the Supabase collection name
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "SyncCache"
    }
}