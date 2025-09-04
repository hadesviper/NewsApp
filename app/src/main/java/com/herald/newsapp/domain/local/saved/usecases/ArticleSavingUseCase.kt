package com.herald.newsapp.domain.local.saved.usecases

import com.herald.newsapp.domain.local.saved.SavingRepository
import com.herald.newsapp.domain.models.HeadlinesModel
import javax.inject.Inject

class ArticleSavingUseCase @Inject constructor(
    private val savingRepository: SavingRepository
) {
    suspend operator fun invoke(article: HeadlinesModel) {
        if (article.isSaved)
            savingRepository.deleteArticle(article.url)
        else
            savingRepository.addArticle(article)
    }
}