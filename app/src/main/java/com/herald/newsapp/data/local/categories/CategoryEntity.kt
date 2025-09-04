package com.herald.newsapp.data.local.categories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_categories")
data class CategoryEntity(
    @PrimaryKey val name: String,
)
