package com.herald.newsapp.domain.models

import com.herald.newsapp.domain.local.saved.usecases.ArticleSavingUseCase
import com.herald.newsapp.domain.local.saved.usecases.GetSavedArticlesUseCase

data class LocalUseCasesModel(
    val articleSavingUseCase: ArticleSavingUseCase,
    val getSavedArticlesUseCase: GetSavedArticlesUseCase
)
