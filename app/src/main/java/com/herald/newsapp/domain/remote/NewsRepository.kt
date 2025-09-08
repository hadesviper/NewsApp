package com.herald.newsapp.domain.remote

import com.herald.newsapp.domain.models.HeadlinesModel

interface NewsRepository {
    suspend fun fetchHeadlines(countryCode: String,category: String): List<HeadlinesModel>
    suspend fun searchHeadlines(query: String, countryCode: String, category: String): List<HeadlinesModel>
}