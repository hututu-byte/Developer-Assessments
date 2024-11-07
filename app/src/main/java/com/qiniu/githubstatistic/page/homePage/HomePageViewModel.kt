package com.qiniu.githubstatistic.page.homePage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qiniu.githubstatistic.service.HomeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(private val homeService: HomeService) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState(emptyList(), refreshing = false, loading = false,""))
    val homeState = _homeState.asStateFlow()

    private val _homeIntent = MutableSharedFlow<HomeIntent>()
    private val homeIntent = _homeIntent.asSharedFlow()

    init {
        viewModelScope.launch {
            homeIntent.collect { intent ->
                processIntent(intent)
            }
        }
    }

    fun sendIntent(intent: HomeIntent) {
        viewModelScope.launch {
            _homeIntent.emit(intent)
        }
    }

    private fun processIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when(intent) {
                //刷新
                HomeIntent.Refresh -> {
                    withContext(Dispatchers.IO){
                        try {
                            // 处理意图并更新状态
                            _homeState.value = _homeState.value.copy(refreshing = true)
                            val newList = homeService.getRandomUsers()
//                            val list = _homeState.value.userList.toMutableList().addAndReturn(newList.data)
                            _homeState.value = _homeState.value.copy(userList = newList.data, refreshing = false)
                            println(_homeState.value)
                        }catch (e:Exception){
                            Log.e("TAG", "processIntent: $e" )
                            _homeState.value = _homeState.value.copy(error = e.message?:"")
                        }

                    }
                }
                // 加载更多
                HomeIntent.Loading -> {
                    withContext(Dispatchers.IO){
                        try {
                            // 处理意图并更新状态
                            _homeState.value = _homeState.value.copy(loading = true)
                            val newList = homeService.getRandomUsers()
                            val list = _homeState.value.userList.toMutableList().addAndReturn(newList.data)
                            Log.e("TAG", "processIntent: ${list.size}")
                            _homeState.value = _homeState.value.copy(userList = list, loading = false)
                        }catch (e:Exception){
                            Log.e("TAG", "processIntent: $e" )
                            _homeState.value = _homeState.value.copy(error = e.message?:"")
                        }
                    }
                }


            }
        }
    }
}

fun <T> MutableList<T>.addAndReturn(list:List<T>): MutableList<T> {
    this.addAll(list)
    return this
}
