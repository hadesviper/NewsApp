package com.herald.newsapp.domain.local.caching.usecases

import com.herald.newsapp.domain.local.caching.CachingRepository
import javax.inject.Inject

class UpdateCachedUseCase @Inject constructor(
    private val cachingRepository: CachingRepository
) {
    suspend operator fun invoke(key: String, isSaved: Boolean) = cachingRepository.updateSavedState(key,isSaved)
}