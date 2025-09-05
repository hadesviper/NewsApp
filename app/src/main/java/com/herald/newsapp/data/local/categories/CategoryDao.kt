package com.herald.newsapp.data.local.categories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    @Query("SELECT * FROM selected_categories")
    suspend fun getSelectedCategories(): List<CategoryEntity>

    @Insert
    suspend fun insertCategories(category: List<CategoryEntity>)
}