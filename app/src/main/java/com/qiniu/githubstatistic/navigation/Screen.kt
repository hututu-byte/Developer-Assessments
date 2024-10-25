package com.qiniu.githubstatistic.navigation

sealed class Screen(val route: String,val description:String) {

    data object HomePage:Screen("HomePage","主页面")

    data object SettingsPage:Screen("SettingsPage","设置页面")

    data object PersonalJudgePage:Screen("PersonalJudgePage","个人评价页面")

}