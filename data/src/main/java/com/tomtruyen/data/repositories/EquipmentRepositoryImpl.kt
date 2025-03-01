package com.tomtruyen.data.repositories

import com.tomtruyen.data.dao.EquipmentDao
import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.data.repositories.interfaces.EquipmentRepository
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class EquipmentRepositoryImpl(
    private val equipmentDao: EquipmentDao
) : EquipmentRepository() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findEquipment() = equipmentDao.findEquipment().mapLatest { equipment ->
        equipment.map(EquipmentUiModel::fromEntity)
    }

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