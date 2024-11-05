package com.qiniu.githubstatistic.page.searchPage

data class SearchPageState(
    //是否获取焦点
    val isFocused:Boolean,
    //搜索内容
    val searchContent:String,
    val searchHistory:List<String>,
    //搜索的用户国家
    val limitCountries:List<String>,
    //用户领域
    val usedTags:List<String>
)

sealed class SearchPageIntent{

    data object ChangeFocus:SearchPageIntent()

    data class Search(val country:List<String>,val tags:List<String>) : SearchPageIntent()

    data object ClearHistory:SearchPageIntent()

    data class AddTags(val tag:String):SearchPageIntent()

    data object AddCountryConstrain:SearchPageIntent()

}