package com.herald.newsapp.data.local.saved

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_news")
data class SavedNewsEntity(
    @PrimaryKey val url: String,
    val title: String,
    val date: String,
    val image: String,
    val sourceNewspaper: String,
    val shortDescription: String
)