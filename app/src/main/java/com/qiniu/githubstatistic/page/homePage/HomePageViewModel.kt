package com.qiniu.githubstatistic.page.homePage

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
class HomePageViewModel @Inject constructor() : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState(emptyList(), ""))
    val homeState = _homeState.asStateFlow()

    private val _homeIntent = MutableSharedFlow<HomeIntent>()
    val homeIntent = _homeIntent.asSharedFlow()

    init {

    }

    fun processIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when(intent) {
                is HomeIntent.FetchData -> {
                    // 处理意图并更新状态
                    val list = _homeState.value.userList.toMutableList().addAndReturn("")
                    _homeState.value = _homeState.value.copy(userList = list)
                }
            }
        }
    }
}

fun <T> MutableList<T>.addAndReturn(vararg elements: T): MutableList<T> {
    this.addAll(elements)
    return this
}
