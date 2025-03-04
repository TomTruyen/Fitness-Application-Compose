package com.tomtruyen.data.repositories.interfaces

import com.tomtruyen.data.entities.Category
import com.tomtruyen.data.models.ui.CategoryUiModel
import com.tomtruyen.data.repositories.BaseRepository
import kotlinx.coroutines.flow.Flow

abstract class CategoryRepository : BaseRepository() {
    override val cacheKey: String
        get() = Category.TABLE_NAME

    abstract fun findCategories(): Flow<List<CategoryUiModel>>
    abstract suspend fun getCategories()
}