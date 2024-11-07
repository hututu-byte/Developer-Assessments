package com.qiniu.githubstatistic.page.searchPage

data class SearchPageState(
    //是否获取焦点
    val isFocused:Boolean = false,
    val chooseCountry:Boolean = false,
    //搜索内容
    val searchContent:String = "",
    val searchHistory:List<String> = emptyList(),
    //搜索的用户国家
    val limitCountries:String = "",
    //用户领域
    val usedTags:List<String> = emptyList(),
    val countryList:List<String> = listOf(
        "China",
        "United States",
        "India",
        "Brazil",
        "Japan",
        "United Kingdom",
        "Germany",
        "France",
        "Canada",
        "Australia",
        "Italy",
        "Russia",
        "South Korea",
        "Spain",
        "Mexico",
        "Indonesia",
        "Korea"
    )
)

sealed class SearchPageIntent{

    data object ChangeFocus:SearchPageIntent()

    data object ChooseCountry:SearchPageIntent()

    data object ClearHistory:SearchPageIntent()

    data class AddTags(val tag:String,val add:Boolean):SearchPageIntent()

    data class AddCountryConstrain(val contry:String):SearchPageIntent()

    data object DismissDialog:SearchPageIntent()

    data object Search:SearchPageIntent()

}