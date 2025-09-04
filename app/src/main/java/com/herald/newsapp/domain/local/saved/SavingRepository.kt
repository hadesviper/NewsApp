package com.herald.newsapp.domain.local.saved

import com.herald.newsapp.domain.models.HeadlinesModel
import kotlinx.coroutines.flow.Flow

interface SavingRepository {
    suspend fun addArticle(article: HeadlinesModel)
    suspend fun deleteArticle(key: String)
    fun getAllNews(): Flow<List<HeadlinesModel>>
}