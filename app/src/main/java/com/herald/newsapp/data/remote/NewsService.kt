package com.herald.newsapp.data.remote

import com.herald.newsapp.common.API_KEY
import com.herald.newsapp.common.RESULT_SORT_BY
import com.herald.newsapp.data.remote.dto.HeadlinesDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("v2/top-headlines")
    suspend fun fetchHeadlines(
        @Query("country") countryCode: String,
        @Query("category") category: String,
        @Query("sortBy") sortBy: String = RESULT_SORT_BY,
        @Query("apiKey") apiKey: String = API_KEY
    ): HeadlinesDTO

    @GET("v2/top-headlines")
    suspend fun searchHeadlines(
        @Query("q") query: String,
        @Query("country") countryCode: String,
        @Query("category") category: String,
        @Query("sortBy") sortBy: String = RESULT_SORT_BY,
        @Query("apiKey") apiKey: String = API_KEY
    ): HeadlinesDTO
}