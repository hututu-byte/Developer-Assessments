package com.qiniu.githubstatistic.model

import kotlinx.serialization.Serializable

@Serializable
data class RandomUsersResponse(
    val code: Int,
    val message: String,
    val data: List<UserDetail>
)

@Serializable
data class UserDetail(
    val githubId: Int,
    val githubUsername: String,
    val talentRank: String,
    val bio: String,
    val country: String,
    val mostCommonTag: String,
    val following: Int,
    val followers: Int,
    val score: Double
)
