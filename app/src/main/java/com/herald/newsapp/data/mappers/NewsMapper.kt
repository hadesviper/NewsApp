package com.herald.newsapp.data.mappers

import com.herald.newsapp.data.local.caching.CachedNewsEntity
import com.herald.newsapp.data.local.saved.SavedNewsEntity
import com.herald.newsapp.data.remote.dto.HeadlinesDTO
import com.herald.newsapp.domain.models.HeadlinesModel

fun HeadlinesDTO.toHeadlinesModel(): List<HeadlinesModel> {
    return articles.map {
        HeadlinesModel(
            url = it.url,
            title = it.title,
            date = it.publishedAt,
            image = it.urlToImage ?: "",
            sourceNewspaper = it.source.name,
            shortDescription = it.description ?: ""
        )
    }
}

fun HeadlinesModel.toSavedNewsEntity(): SavedNewsEntity {
    return SavedNewsEntity(
        url = url,
        title = title,
        date = date,
        image = image,
        sourceNewspaper = sourceNewspaper,
        shortDescription = shortDescription,
    )
}

fun HeadlinesModel.toCachedNewsEntity(): CachedNewsEntity {
    return CachedNewsEntity(
        url = url,
        title = title,
        date = date,
        image = image,
        isSaved = isSaved,
        sourceNewspaper = sourceNewspaper,
        shortDescription = shortDescription,
    )
}

fun CachedNewsEntity.toHeadlinesModel(): HeadlinesModel {
    return HeadlinesModel(
        url = url,
        title = title,
        date = date,
        image = image,
        isSaved = isSaved,
        sourceNewspaper = sourceNewspaper,
        shortDescription = shortDescription,
    )
}

fun SavedNewsEntity.toHeadlinesModel(): HeadlinesModel {
    return HeadlinesModel(
        url = url,
        title = title,
        date = date,
        image = image,
        isSaved = true,
        sourceNewspaper = sourceNewspaper,
        shortDescription = shortDescription,
    )
}