package com.herald.newsapp.data.mappers

import com.herald.newsapp.data.remote.dto.HeadlinesDTO
import com.herald.newsapp.domain.models.HeadlinesModel

fun HeadlinesDTO.toHeadlinesModel(): List<HeadlinesModel> {
    return articles.map {
        HeadlinesModel(
            id = it.url,
            title = it.title,
            date = it.publishedAt,
            image = it.urlToImage ?: "",
            sourceNewspaper = it.source.name,
            shortDescription = it.description ?: ""
        )
    }
}