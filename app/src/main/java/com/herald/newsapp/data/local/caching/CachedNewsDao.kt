package com.herald.newsapp.data.local.caching

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedNewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNews(news: List<CachedNewsEntity>)

    @Query("UPDATE cached_news SET isSaved = :isSaved WHERE url = :url")
    suspend fun updateSavedState(url: String, isSaved: Boolean)

    @Query("DELETE FROM cached_news")
    suspend fun deleteAllNews()

    @Query("""
        SELECT c.url,c.title,c.date,c.image,c.sourceNewspaper,c.shortDescription,
        CASE WHEN s.url IS NOT NULL THEN 1 ELSE 0 END AS isSaved
        FROM cached_news AS c
        LEFT JOIN saved_news  AS s 
        ON c.url = s.url
        ORDER BY c.date DESC
    """)
    fun getAllNews(): Flow<List<CachedNewsEntity>>
}