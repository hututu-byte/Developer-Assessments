package com.qiniu.githubstatistic.page.searchPage

data class SearchPageState(
    //是否获取焦点
    val isFocused:Boolean,
    val chooseCountry:Boolean = false,
    //搜索内容
    val searchContent:String,
    val searchHistory:List<String>,
    //搜索的用户国家
    val limitCountries:String,
    //用户领域
    val usedTags:List<String>
)

sealed class SearchPageIntent{

    data object ChangeFocus:SearchPageIntent()

    data object ChooseCountry:SearchPageIntent()

    data object ClearHistory:SearchPageIntent()

    data class AddTags(val tag:String,val add:Boolean):SearchPageIntent()

    data object AddCountryConstrain:SearchPageIntent()

}