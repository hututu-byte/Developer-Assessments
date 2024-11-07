package com.qiniu.githubstatistic.page.userDetailedPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qiniu.githubstatistic.model.UserDetailMore
import com.qiniu.githubstatistic.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userService: UserService
) : ViewModel() {
    private val _userDetailMore =
        MutableStateFlow(UserDetailMore(0, "", "", "", "", "", 0, 0, 0.0, 0, 0, 0, emptyList()))
    val userDetailMore = _userDetailMore


    fun getUserDetailMore(id:Int) {
        viewModelScope.launch {
            _userDetailMore.value = userService.getUserInfo(id).data
        }
    }

}