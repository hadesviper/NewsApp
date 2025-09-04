package com.herald.newsapp.data.local.saved

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticle(article: SavedNewsEntity)

    @Query("DELETE FROM saved_news where url = :key")
    suspend fun deleteArticle(key: String)

    @Query("Select * from saved_news order by `date` asc")
    fun getAllNews(): Flow<List<SavedNewsEntity>>
}