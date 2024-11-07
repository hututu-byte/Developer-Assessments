package com.qiniu.githubstatistic.model

import kotlinx.serialization.Serializable

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

@Serializable
data class UserDetailMore(
    val githubId: Int,
    val githubUsername: String,
    val talentRank: String,
    val bio: String,
    val country: String,
    val mostCommonTag: String,
    val following: Int,
    val followers: Int,
    val score: Double,
    val totalStars: Int,
    val totalForks: Int,
    val totalProject: Int,
    val topProjects: List<Repository>
)
