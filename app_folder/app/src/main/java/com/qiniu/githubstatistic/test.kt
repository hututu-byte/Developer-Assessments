package com.qiniu.githubstatistic

import com.qiniu.githubstatistic.model.UserDetail
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


//fun main() {
//    runBlocking { val request = okhttp3.Request.Builder()
//        .url("http://10.33.23.144:8080/api/developers/random")
//        .build()
//
//        val okHttpClient = OkHttpClient.Builder()
//            .connectTimeout(3000L, TimeUnit.MILLISECONDS)
//            .writeTimeout(10, TimeUnit.SECONDS)
//            .retryOnConnectionFailure(true)
//            .build()
//
//        val response = okHttpClient.newCall(request).execute()
//        println(response.body) }
//
//}

//fun main()
//{
//    val userDetail = UserDetail(21373505, "mertcakmak2", "LOW", "Java Developer #SpringBoot #Go", "Ankara", "Go, Java, Javascript", 54, 128, 80.6)
//    val string = Json.encodeToString(UserDetail.serializer(), userDetail)
//    val decode = Json.decodeFromString(UserDetail.serializer(), string)
//    println(string.substring(82,100))
//    println(decode)
//}

//{
//  "githubId": 21373505,
//  "githubUsername": "mertcakmak2",
//  "talentRank": "LOW",
//  "bio": "Java Developer #SpringBoot #Go",
//  "country": "Ankara",
//  "mostCommonTag": "Go, Java, Javascript",
//  "following": 54,
//  "followers": 128,
//  "score": 80.60000000000001
//}