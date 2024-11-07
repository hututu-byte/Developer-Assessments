package com.qiniu.githubstatistic.page.searchPage

import android.util.Log
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
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    private val _searchState = MutableStateFlow(SearchPageState())
    val searchState = _searchState.asStateFlow()

    private val _searchIntent = MutableSharedFlow<SearchPageIntent>()
    private val searchIntent = _searchIntent.asSharedFlow()

    init {
        viewModelScope.launch {
            searchIntent.collect { intent ->
                println(intent)
                processIntent(intent)
            }
        }
    }


    fun sendIntent(intent: SearchPageIntent) {
        viewModelScope.launch {
            _searchIntent.emit(intent)
        }
    }

    fun getSearchKey():String
    {
        val searchKey = SearchKey(
            searchContent = searchState.value.searchContent,
            searchHistory = searchState.value.searchHistory,
            limitCountries = searchState.value.limitCountries,
            usedTags = searchState.value.usedTags
        )
        return Json.encodeToString(SearchKey.serializer(), searchKey)
    }

    private fun processIntent(intent: SearchPageIntent) {
        viewModelScope.launch {
            when (intent) {
                is SearchPageIntent.AddCountryConstrain -> {
                    _searchState.value = _searchState.value.copy(
                        limitCountries = intent.contry
                    )
                }

                is SearchPageIntent.AddTags -> {
                    if (intent.add) {
                        _searchState.value = _searchState.value.copy(
                            usedTags = _searchState.value.usedTags + intent.tag
                        )
                    }else{
                        _searchState.value = _searchState.value.copy(
                            usedTags = _searchState.value.usedTags - intent.tag
                        )
                    }
                }

                SearchPageIntent.ChangeFocus -> {
                    _searchState.value = _searchState.value.copy(
                        isFocused = !_searchState.value.isFocused
                    )
                    println(_searchState.value)
                }

                SearchPageIntent.ClearHistory -> {

                }

                SearchPageIntent.ChooseCountry -> {
                    _searchState.value = _searchState.value.copy(
                        chooseCountry = true
                    )
                }

                SearchPageIntent.DismissDialog -> {
                    _searchState.value = _searchState.value.copy(
                        chooseCountry = false
                    )
                }

                SearchPageIntent.Search -> {
                    _searchState.value = SearchPageState()
                }
            }
        }
    }
}