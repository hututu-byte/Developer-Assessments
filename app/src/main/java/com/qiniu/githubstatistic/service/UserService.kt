package com.qiniu.githubstatistic.service

import com.qiniu.githubstatistic.model.ListResponse
import com.qiniu.githubstatistic.model.Response
import com.qiniu.githubstatistic.model.UserDetailMore
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/api/developers/basic-info/{githubId}")
    suspend fun getUserInfo(@Path("githubId") githubId: Int): Response<UserDetailMore>

}