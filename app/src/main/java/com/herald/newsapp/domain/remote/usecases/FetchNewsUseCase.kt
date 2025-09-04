package com.herald.newsapp.domain.remote.usecases

import android.util.Log
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
            val resultedNews = coroutineScope {
                categories.map {
                    async { newsRepository.fetchHeadlines(countryCode, it) }
                }.awaitAll().flatten()
            }
            Log.i("TAG", "invoke: resulted news == $resultedNews")
            emit(Resource.Success(resultedNews))
        } catch (e: Exception) {
            Log.i("TAG", "invoke: resulted error == ${e.message}")
            emit(Resource.Error(e))
        }
    }
}