package com.herald.newsapp.di

import android.content.Context
import com.herald.newsapp.common.StringsProvider
import com.herald.newsapp.domain.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StringProviderModule {
    @Provides
    @Singleton
    fun provideDefaultStringProvider(
        @ApplicationContext context: Context
    ): ResourceProvider {
        return StringsProvider(context)
    }
}