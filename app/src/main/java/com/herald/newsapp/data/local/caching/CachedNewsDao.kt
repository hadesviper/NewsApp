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

    @Query("Select * from cached_news order by `date` asc")
    fun getAllNews(): Flow<List<CachedNewsEntity>>
}