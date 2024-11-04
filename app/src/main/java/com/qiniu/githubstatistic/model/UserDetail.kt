package com.qiniu.githubstatistic.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDetail(
    val userName : String,
    val avatar : String,
    val follows:Int,
    val followers:Int,
    val repository:List<Repository>
)
