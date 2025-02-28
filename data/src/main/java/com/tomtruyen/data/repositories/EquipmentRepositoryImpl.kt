package com.tomtruyen.data.repositories

import com.tomtruyen.data.dao.EquipmentDao
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.repositories.interfaces.EquipmentRepository
import io.github.jan.supabase.postgrest.from

class EquipmentRepositoryImpl(
    private val equipmentDao: EquipmentDao
) : EquipmentRepository() {
    override fun findEquipment() = equipmentDao.findEquipment()

    override suspend fun getEquipment() {
        supabase.from(Equipment.TABLE_NAME)
            .select()
            .decodeList<Equipment>()
            .let { equipment ->
                launchWithCacheTransactions {
                    equipmentDao.saveAll(equipment)
                }
            }
    }
}