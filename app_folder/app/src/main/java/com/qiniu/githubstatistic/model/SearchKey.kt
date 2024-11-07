package com.qiniu.githubstatistic.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchKey(
    //搜索内容
    val searchContent:String,
    val searchHistory:List<String>,
    //搜索的用户国家
    val limitCountries:String,
    //用户领域
    val usedTags:List<String>
)
