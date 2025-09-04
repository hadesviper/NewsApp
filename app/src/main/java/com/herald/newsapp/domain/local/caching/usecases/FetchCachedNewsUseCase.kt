package com.herald.newsapp.domain.local.caching.usecases

import com.herald.newsapp.domain.local.caching.CachingRepository
import javax.inject.Inject

class FetchCachedNewsUseCase @Inject constructor(
    private val cachingRepository: CachingRepository
) {
    operator fun invoke () = cachingRepository.getAllNews()
}