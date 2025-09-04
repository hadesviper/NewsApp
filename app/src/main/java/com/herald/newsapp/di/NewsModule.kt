package com.herald.newsapp.di

import com.herald.newsapp.common.BASE_URL
import com.herald.newsapp.data.remote.NewsRepoImpl
import com.herald.newsapp.data.remote.NewsService
import com.herald.newsapp.domain.remote.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun getNewsService(retrofit: Retrofit): NewsService {
        return retrofit.create(NewsService::class.java)
    }

    @Provides
    @Singleton
    fun getNewsRepoImpl(retroService: NewsService): NewsRepository {
        return NewsRepoImpl(retroService)
    }
}