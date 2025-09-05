package com.herald.newsapp.domain.remote.usecases

import com.herald.newsapp.common.Resource
import com.herald.newsapp.domain.remote.NewsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(
        countryCode: String,
        categories: List<String>
    ) = flow {
        try {
            emit(Resource.Loading)
            if (categories.isEmpty())
                throw Exception("No categories selected")
            val resultedNews = coroutineScope {
                categories.map {
                    async { newsRepository.fetchHeadlines(countryCode, it) }
                }.awaitAll().flatten()
            }
            emit(Resource.Success(resultedNews))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}