package com.herald.newsapp.data.local.saved

import com.herald.newsapp.data.mappers.toHeadlinesModel
import com.herald.newsapp.data.mappers.toSavedNewsEntity
import com.herald.newsapp.domain.local.saved.SavingRepository
import com.herald.newsapp.domain.models.HeadlinesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedRepoImpl (
    private val savedNewsDao: SavedNewsDao
): SavingRepository {
    override suspend fun addArticle(article: HeadlinesModel) {
        savedNewsDao.addArticle(article.toSavedNewsEntity())
    }

    override suspend fun deleteArticle(key: String) {
        savedNewsDao.deleteArticle(key)
    }

    override fun getAllNews(): Flow<List<HeadlinesModel>> {
        return savedNewsDao.getAllNews().map {
            it.map { entity ->
                entity.toHeadlinesModel()
            }
        }
    }
}