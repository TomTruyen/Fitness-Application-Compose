package com.tomtruyen.data.repositories

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.models.mappers.CategoryUiModelMapper
import com.tomtruyen.data.repositories.interfaces.CategoryRepository
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class CategoryRepositoryImpl : CategoryRepository() {
    private val dao = database.categoryDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findCategories() = dao.findCategories().mapLatest { categories ->
        categories.map(CategoryUiModelMapper::fromEntity)
    }

    override suspend fun getCategories() {
        fetch {
            supabase.from(Category.TABLE_NAME)
                .select()
                .decodeList<Category>()
                .let { categories ->
                    cacheTransaction {
                        dao.saveAll(categories)
                    }
                }
        }
    }
}