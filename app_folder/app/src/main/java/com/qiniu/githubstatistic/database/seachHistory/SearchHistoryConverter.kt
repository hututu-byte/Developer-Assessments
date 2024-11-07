package com.qiniu.githubstatistic.database.seachHistory

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class SearchHistoryConverter {
    @TypeConverter
    fun fromJson(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun toJson(value: List<String>): String {
        return Json.encodeToString(value)
    }
}