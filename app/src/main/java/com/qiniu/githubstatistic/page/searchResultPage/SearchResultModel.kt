package com.qiniu.githubstatistic.page.searchResultPage

import com.qiniu.githubstatistic.model.UserDetail

data class SearchResultState (
    val userList: List<UserDetail> = emptyList(),
    val isSearching : Boolean = true,
    val isAnimVisible : Boolean = true,
    val error: String = "",
    val failed:Boolean = false,
    val showDialog: Boolean = false
)

sealed class SearchResultIntent {
    data class Search(val searchKey: String) : SearchResultIntent()
    data object DismissDialog : SearchResultIntent()
    data object CancelAnimation: SearchResultIntent()
}