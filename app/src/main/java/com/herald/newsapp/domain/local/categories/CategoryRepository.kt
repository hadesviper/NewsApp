package com.herald.newsapp.domain.local.categories

interface CategoryRepository {
    suspend fun getSelectedCategories(): List<String>
    suspend fun insertCategories(categories: List<String>)
}