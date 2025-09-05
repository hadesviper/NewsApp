package com.herald.newsapp.presentation.states

import com.herald.newsapp.domain.models.HeadlinesModel

data class NewsState(
    val isLoading: Boolean = false,
    val news: List<HeadlinesModel>? = null,
    val exception: Exception? = null
)
