package com.herald.newsapp.domain.local.caching.usecases

import com.herald.newsapp.domain.local.caching.CachingRepository
import com.herald.newsapp.domain.models.HeadlinesModel
import javax.inject.Inject

class AddCachedNewsUseCase @Inject constructor(
    private val cachingRepository: CachingRepository
) {
    suspend operator fun invoke(news: List<HeadlinesModel>){
        cachingRepository.addNews(news)
    }
}