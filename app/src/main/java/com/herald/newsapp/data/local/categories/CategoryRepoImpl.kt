package com.herald.newsapp.data.local.categories

import com.herald.newsapp.domain.local.categories.CategoryRepository

class CategoryRepoImpl (
    private val categoryDao: CategoryDao
): CategoryRepository {
    override suspend fun getSelectedCategories(): List<String> {
        return categoryDao.getSelectedCategories().map {
            it.name
        }
    }
    override suspend fun insertCategories(categories: List<String>) {
        val categoryEntities = categories.map {
            CategoryEntity(it)
        }
        categoryDao.insertCategories(categoryEntities)
    }
}