package com.herald.newsapp.domain.models

data class HeadlinesModel(
    val id: String,
    val title: String,
    val date: String,
    val image: String,
    val isSaved: Boolean = false,
    val sourceNewspaper: String,
    val shortDescription: String
)
