package com.qiniu.githubstatistic.service

import com.qiniu.githubstatistic.model.ListResponse
import com.qiniu.githubstatistic.model.UserDetail
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("/api/developers/search")
    suspend fun searchDevelopers(
        @Query("githubUsername") name: String,
        @Query("language") language: List<String>,
        @Query("country") country: String,
        @Query("sort") sort: String = "desc"
    ): ListResponse<UserDetail>

}