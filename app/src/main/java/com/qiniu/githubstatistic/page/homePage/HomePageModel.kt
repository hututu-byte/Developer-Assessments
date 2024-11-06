package com.qiniu.githubstatistic.page.homePage

import com.qiniu.githubstatistic.model.UserDetail

data class HomeState(
    val userList: List<UserDetail>,
    val refreshing:Boolean,
    val loading:Boolean,
    val error:String
)

sealed class HomeIntent{
    data object Refresh : HomeIntent()
    data object Loading : HomeIntent()
}