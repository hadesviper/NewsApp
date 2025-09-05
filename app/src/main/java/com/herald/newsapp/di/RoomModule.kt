package com.herald.newsapp.di

import android.app.Application
import androidx.room.Room
import com.herald.newsapp.common.DB_NAME
import com.herald.newsapp.data.local.AppDatabase
import com.herald.newsapp.data.local.caching.CachedNewsDao
import com.herald.newsapp.data.local.caching.CachingRepoImpl
import com.herald.newsapp.data.local.categories.CategoryDao
import com.herald.newsapp.data.local.categories.CategoryRepoImpl
import com.herald.newsapp.data.local.saved.SavedNewsDao
import com.herald.newsapp.data.local.saved.SavedRepoImpl
import com.herald.newsapp.domain.local.caching.CachingRepository
import com.herald.newsapp.domain.local.categories.CategoryRepository
import com.herald.newsapp.domain.local.saved.SavingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, DB_NAME).build()
    }

    @Provides
    @Singleton
    fun provideCachedNewsDao(appDatabase: AppDatabase) = appDatabase.cachedNewsDao()

    @Provides
    @Singleton
    fun provideSavedNewsDao(appDatabase: AppDatabase) = appDatabase.savedNewsDao()

    @Provides
    @Singleton
    fun provideCategoryDao(appDatabase: AppDatabase) = appDatabase.categoryDao()

    @Provides
    @Singleton
    fun providesNewsSavingRepo(savedNewsDao: SavedNewsDao): SavingRepository = SavedRepoImpl(savedNewsDao)

    @Provides
    @Singleton
    fun providesNewsCachingRepo(cachedNewsDao: CachedNewsDao): CachingRepository = CachingRepoImpl(cachedNewsDao)

    @Provides
    @Singleton
    fun providesCategoryRepo(categoryDao: CategoryDao): CategoryRepository = CategoryRepoImpl(categoryDao)

}