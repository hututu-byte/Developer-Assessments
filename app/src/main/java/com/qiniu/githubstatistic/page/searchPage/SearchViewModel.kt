package com.qiniu.githubstatistic.page.searchPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    private val _searchState = MutableStateFlow(
        SearchPageState(
            isFocused = false,
            searchContent = "",
            searchHistory = emptyList(),
            limitCountries = emptyList(),
            usedTags = emptyList()
        )
    )
    val searchState = _searchState.asStateFlow()

    private val _searchIntent = MutableSharedFlow<SearchPageIntent>()
    private val searchIntent = _searchIntent.asSharedFlow()

    init {
        viewModelScope.launch {
            searchIntent.collect { intent ->
                processIntent(intent)
            }
        }
    }


    fun sendIntent(intent: SearchPageIntent) {
        viewModelScope.launch {
            _searchIntent.emit(intent)
        }
    }

    private fun processIntent(intent: SearchPageIntent) {
        viewModelScope.launch {
            when (intent) {
                SearchPageIntent.AddCountryConstrain -> {

                }

                SearchPageIntent.AddTags -> {

                }

                SearchPageIntent.ChangeFocus -> {

                }

                SearchPageIntent.ClearHistory -> {

                }

                SearchPageIntent.Search -> {

                }
            }
        }
    }

}