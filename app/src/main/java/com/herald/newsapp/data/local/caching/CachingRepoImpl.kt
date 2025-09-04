package com.herald.newsapp.data.local.caching

import com.herald.newsapp.data.mappers.toCachedNewsEntity
import com.herald.newsapp.data.mappers.toHeadlinesModel
import com.herald.newsapp.domain.local.caching.CachingRepository
import com.herald.newsapp.domain.models.HeadlinesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CachingRepoImpl (
    private val cachedNewsDao: CachedNewsDao
): CachingRepository {
    override suspend fun addNews(news: List<HeadlinesModel>) {
        val newsEntity = news.map {
            it.toCachedNewsEntity()
        }
        cachedNewsDao.addNews(newsEntity)
    }

    override suspend fun updateSavedState(url: String, isSaved: Boolean) {
        cachedNewsDao.updateSavedState(url, isSaved)
    }

    override suspend fun deleteAllNews() {
        cachedNewsDao.deleteAllNews()
    }

    override fun getAllNews(): Flow<List<HeadlinesModel>> {
        return cachedNewsDao.getAllNews().map {
            it.map { entity ->
                entity.toHeadlinesModel()
            }
        }
    }
}