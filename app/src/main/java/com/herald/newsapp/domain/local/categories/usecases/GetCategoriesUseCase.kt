package com.herald.newsapp.domain.local.categories.usecases

import com.herald.newsapp.domain.local.categories.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<String> {
        return categoryRepository.getSelectedCategories()
    }
}