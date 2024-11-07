package com.qiniu.githubstatistic.page.searchResultPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qiniu.githubstatistic.model.SearchKey
import com.qiniu.githubstatistic.service.SearchService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(private val searchService: SearchService):ViewModel() {
    private val _searchResultPageState = MutableStateFlow(SearchResultState())
    val searchResultPageState = _searchResultPageState.asStateFlow()

    private val _searchIntent = MutableSharedFlow<SearchResultIntent>()
    private val searchIntent = _searchIntent.asSharedFlow()

    init {
        viewModelScope.launch {
            searchIntent.collect { intent ->
                println("searchIntent.collect { intent ->")
                processIntent(intent)
            }
        }
    }

    fun sendIntent(intent: SearchResultIntent) {
        viewModelScope.launch {
            _searchIntent.emit(intent)
        }

    }

    private fun processIntent(intent: SearchResultIntent) {
        viewModelScope.launch {
            when (intent) {
                is SearchResultIntent.Search -> {
                    val searchKey = Json.decodeFromString(SearchKey.serializer(), intent.searchKey)
                    println(searchKey)
                    withContext(Dispatchers.IO){
                        try {
                            val list = searchService.searchDevelopers(
                                searchKey.searchContent,
                                language = searchKey.usedTags,
                                country = searchKey.limitCountries
                            ).data
                            _searchResultPageState.value = _searchResultPageState.value.copy(
                                isSearching = false
                            )
                            kotlinx.coroutines.delay(800)
                            _searchResultPageState.value = _searchResultPageState.value.copy(
                                userList = list
                            )
                        }catch (e:Exception){
                            _searchResultPageState.value = _searchResultPageState.value.copy(
                                isSearching = false,
                                error = "搜索失败",
                                showDialog = true
                            )
                        }
                    }
                }

                is SearchResultIntent.DismissDialog -> {
                    _searchResultPageState.value =
                        _searchResultPageState.value.copy(showDialog = false)
                }

                SearchResultIntent.CancelAnimation -> {
                    _searchResultPageState.value = _searchResultPageState.value.copy(isAnimVisible = false)
                }
            }
        }
    }

}