package com.qiniu.githubstatistic.database.seachHistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history")
    fun getAllSearchHistory(): Flow<List<SearchHistory>>

    @Query("SELECT COUNT(*) FROM search_history")
    suspend fun getSearchHistoryCount(): Int

    @Query("DELETE FROM search_history WHERE id = (SELECT id FROM search_history ORDER BY id ASC LIMIT 1)")
    suspend fun deleteOldestSearchHistory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(entry: SearchHistory)

    @Transaction
    suspend fun insertSearchHistory(userName: String, country: List<String>, tags: List<String>) {
        // 检查记录数量
        if (getSearchHistoryCount() >= 10) {
            // 删除最早的记录
            deleteOldestSearchHistory()
        }
        // 插入新的记录
        insertSearchHistory(SearchHistory(userName = userName, country = country, tags = tags))
    }
}