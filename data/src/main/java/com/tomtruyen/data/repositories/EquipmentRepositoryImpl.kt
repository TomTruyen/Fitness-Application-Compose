package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Equipment
import com.tomtruyen.data.models.ui.EquipmentUiModel
import com.tomtruyen.data.repositories.interfaces.EquipmentRepository
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class EquipmentRepositoryImpl : EquipmentRepository() {
    private val dao = database.equipmentDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findEquipment() = dao.findEquipment().mapLatest { equipment ->
        equipment.map(EquipmentUiModel::fromEntity)
    }

    override suspend fun getEquipment() {
        fetch {
            supabase.from(Equipment.TABLE_NAME)
                .select()
                .decodeList<Equipment>()
                .let { equipment ->
                    cacheTransaction {
                        dao.saveAll(equipment)
                    }
                }
        }
    }
}