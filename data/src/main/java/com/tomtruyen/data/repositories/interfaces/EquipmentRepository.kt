package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.repositories.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class EquipmentRepository : BaseRepository() {
    override val identifier: String
        get() = Equipment.TABLE_NAME

    abstract fun findEquipment(): Flow<List<Equipment>>
    abstract suspend fun getEquipment()
}