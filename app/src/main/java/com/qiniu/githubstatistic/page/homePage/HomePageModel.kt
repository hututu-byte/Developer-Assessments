package com.qiniu.githubstatistic.page.homePage

data class HomeState(
    val userList: List<String>,
    val error:String
)

sealed class HomeIntent{
    data object FetchData : HomeIntent()
}