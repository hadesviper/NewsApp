package com.herald.newsapp.di

import com.herald.newsapp.domain.local.caching.CachingRepository
import com.herald.newsapp.domain.local.saved.SavingRepository
import com.herald.newsapp.domain.local.saved.usecases.ArticleSavingUseCase
import com.herald.newsapp.domain.local.saved.usecases.GetSavedArticlesUseCase
import com.herald.newsapp.domain.models.LocalUseCasesModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalUseCasesModule {
    @Provides
    @Singleton
    fun provideLocalUseCases(
        cachingRepository: CachingRepository,
        savingRepository: SavingRepository
    ): LocalUseCasesModel {
        return LocalUseCasesModel(
            articleSavingUseCase = ArticleSavingUseCase(savingRepository),
            getSavedArticlesUseCase = GetSavedArticlesUseCase(savingRepository)
        )
    }
}