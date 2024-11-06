package com.qiniu.githubstatistic.service

import com.qiniu.githubstatistic.model.RandomUsersResponse
import com.qiniu.githubstatistic.model.UserDetail
import retrofit2.http.GET


interface HomeService {

    @GET("/api/developers/random")
    suspend fun getRandomUsers():RandomUsersResponse
}