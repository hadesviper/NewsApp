package com.herald.newsapp.domain.local.categories.usecases

import com.herald.newsapp.domain.local.categories.CategoryRepository
import javax.inject.Inject

class SaveCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categories: List<String>) {
        categoryRepository.insertCategories(categories)
    }
}