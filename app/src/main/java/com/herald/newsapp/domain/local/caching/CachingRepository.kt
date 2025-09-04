package com.herald.newsapp.domain.local.caching

import com.herald.newsapp.domain.models.HeadlinesModel
import kotlinx.coroutines.flow.Flow


interface CachingRepository {
    suspend fun addNews(news: List<HeadlinesModel>)
    suspend fun updateSavedState(url: String, isSaved: Boolean)
    suspend fun deleteAllNews()
    fun getAllNews(): Flow<List<HeadlinesModel>>
}