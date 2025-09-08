package com.herald.newsapp.domain.remote.usecases

import com.herald.newsapp.common.Resource
import com.herald.newsapp.domain.models.SearchQuery
import com.herald.newsapp.domain.remote.NewsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(searchQuery: SearchQuery) = flow {
        try {
            emit(Resource.Loading)
            val resultedNews = if (searchQuery.categories.isEmpty()) {
                newsRepository.searchHeadlines(searchQuery.query, searchQuery.country,"")
            }
            else {
                coroutineScope {
                    searchQuery.categories.map {
                        async { newsRepository.searchHeadlines(searchQuery.query,searchQuery.country,it) }
                    }.awaitAll().flatten()
                }
            }
            emit(Resource.Success(resultedNews))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}