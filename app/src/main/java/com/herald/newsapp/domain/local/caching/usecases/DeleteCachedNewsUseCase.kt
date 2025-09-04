package com.herald.newsapp.domain.local.caching.usecases

import com.herald.newsapp.domain.local.caching.CachingRepository
import javax.inject.Inject

class DeleteCachedNewsUseCase @Inject constructor(
    private val cachingRepository: CachingRepository
) {
    suspend operator fun invoke(){
        cachingRepository.deleteAllNews()
    }
}