package com.herald.newsapp.domain.local.saved.usecases

import com.herald.newsapp.domain.local.saved.SavingRepository
import javax.inject.Inject

class GetSavedArticlesUseCase @Inject constructor(
    private val savingRepository: SavingRepository
) {
    operator fun invoke() = savingRepository.getAllNews()
}