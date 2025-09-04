package com.herald.newsapp.data.local.caching

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_news")
data class CachedNewsEntity(
    @PrimaryKey val url: String,
    val title: String,
    val date: String,
    val image: String,
    val isSaved: Boolean,
    val sourceNewspaper: String,
    val shortDescription: String
)
