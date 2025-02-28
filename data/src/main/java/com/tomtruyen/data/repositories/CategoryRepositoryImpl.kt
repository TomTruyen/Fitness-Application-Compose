package com.tomtruyen.data.repositories

import com.tomtruyen.data.dao.CategoryDao
import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.repositories.interfaces.CategoryRepository
import io.github.jan.supabase.postgrest.from

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository() {
    override fun findCategories() = categoryDao.findCategories()

    override suspend fun getCategories() {
        supabase.from(Category.TABLE_NAME)
            .select()
            .decodeList<Category>()
            .let { categories ->
                launchWithCacheTransactions {
                    categoryDao.saveAll(categories)
                }
            }
    }
}