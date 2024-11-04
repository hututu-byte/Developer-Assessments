package com.qiniu.githubstatistic.database.seachHistory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val userName:String,
    val country:List<String>,
    val tags:List<String>,
)