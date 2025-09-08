package com.herald.newsapp.domain.models

data class SearchQuery(
    val query: String = "",
    val country: String = "",
    val categories: List<String> = emptyList()
)
