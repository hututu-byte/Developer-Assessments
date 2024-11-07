package com.qiniu.githubstatistic.model

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
    val repositoryName:String,
    val description:String,
    val stars:Int,
    val language:String
)
