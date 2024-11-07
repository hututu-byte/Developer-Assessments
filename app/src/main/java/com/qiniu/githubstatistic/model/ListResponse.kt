package com.qiniu.githubstatistic.model

import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T>(
    val code: Int,
    val message: String,
    val data: List<T>
)

@Serializable
data class Response<T>(
    val code: Int,
    val message: String,
    val data: T
)
