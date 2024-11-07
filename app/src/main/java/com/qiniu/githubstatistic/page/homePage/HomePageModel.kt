package com.qiniu.githubstatistic.page.homePage

import com.qiniu.githubstatistic.model.UserDetail

data class HomeState(
    val userList: List<UserDetail> = emptyList(),
    val refreshing:Boolean = false,
    val loading:Boolean = false,
    val error:String = "",
    val showDialog:Boolean = false
)

sealed class HomeIntent{
    data object Refresh : HomeIntent()
    data object Loading : HomeIntent()
    data object DismissDialog : HomeIntent()
}