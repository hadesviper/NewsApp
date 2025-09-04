package com.herald.newsapp.data.remote

import com.herald.newsapp.data.mappers.toHeadlinesModel
import com.herald.newsapp.domain.models.HeadlinesModel
import com.herald.newsapp.domain.remote.NewsRepository
import javax.inject.Inject

class NewsRepoImpl @Inject constructor(
    private val newsService: NewsService
): NewsRepository {
    override suspend fun fetchHeadlines(
        countryCode: String,
        category: String
    ): List<HeadlinesModel> {
        return newsService.fetchHeadlines(countryCode, category).toHeadlinesModel()
    }

    override suspend fun searchHeadlines(
        query: String,
        countryCode: String,
        category: String
    ): List<HeadlinesModel> {
        return newsService.searchHeadlines(query, countryCode, category).toHeadlinesModel()
    }
}