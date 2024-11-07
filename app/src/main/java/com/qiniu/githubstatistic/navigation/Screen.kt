package com.qiniu.githubstatistic.navigation

sealed class Screen(val route: String,val description:String) {

    data object HomePage:Screen("HomePage","主页面")

    data object UserDetailedPage:Screen("UserDetailedPage","个人评价页面")

    data object SearchPage:Screen("SearchPage","搜索页面")

    data object SearchResultPage:Screen("SearchResultPage","搜索结果页面")

}