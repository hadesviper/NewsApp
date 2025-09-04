package com.herald.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.herald.newsapp.data.local.caching.CachedNewsDao
import com.herald.newsapp.data.local.caching.CachedNewsEntity
import com.herald.newsapp.data.local.categories.CategoryDao
import com.herald.newsapp.data.local.categories.CategoryEntity
import com.herald.newsapp.data.local.saved.SavedNewsDao
import com.herald.newsapp.data.local.saved.SavedNewsEntity

@Database(entities = [CategoryEntity::class,CachedNewsEntity::class, SavedNewsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun cachedNewsDao(): CachedNewsDao
    abstract fun savedNewsDao(): SavedNewsDao
}