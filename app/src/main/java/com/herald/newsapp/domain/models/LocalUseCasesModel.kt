package com.herald.newsapp.domain.models

import com.herald.newsapp.domain.local.caching.usecases.AddCachedNewsUseCase
import com.herald.newsapp.domain.local.caching.usecases.DeleteCachedNewsUseCase
import com.herald.newsapp.domain.local.caching.usecases.FetchCachedNewsUseCase
import com.herald.newsapp.domain.local.caching.usecases.UpdateCachedUseCase
import com.herald.newsapp.domain.local.saved.usecases.ArticleSavingUseCase
import com.herald.newsapp.domain.local.saved.usecases.GetSavedArticlesUseCase

data class LocalUseCasesModel(
    val articleSavingUseCase: ArticleSavingUseCase,
    val getSavedArticlesUseCase: GetSavedArticlesUseCase,
    val deleteCachedNewsUseCase: DeleteCachedNewsUseCase,
    val fetchCachedNewsUseCase: FetchCachedNewsUseCase,
    val addCachedNewsUseCase: AddCachedNewsUseCase,
    val updateCachedUseCase: UpdateCachedUseCase
)
