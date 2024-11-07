package com.qiniu.githubstatistic.page.searchResultPage

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.qiniu.githubstatistic.customView.LoadingAnimation
import com.qiniu.githubstatistic.model.UserDetail
import com.qiniu.githubstatistic.navigation.Screen
import com.qiniu.githubstatistic.page.homePage.PersonalJudgeCard
import kotlinx.serialization.json.Json

@Composable
fun SearchResultsPage(navHostController:NavHostController,searchKey:String,viewModel: SearchResultViewModel = hiltViewModel()) {
    val state by viewModel.searchResultPageState.collectAsState()
    LaunchedEffect (true){
        viewModel.sendIntent(SearchResultIntent.Search(searchKey))
    }
    val lazyListState = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize()) {
        // 显示加载动画的Box
        AnimatedVisibility(
            visible = state.isAnimVisible,
            modifier = Modifier.zIndex(1f) // 设置更高的层级
        ) {
            LoadingAnimation(isCompleted = !state.isSearching) {
                viewModel.sendIntent(SearchResultIntent.CancelAnimation)
            }
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding(),
            state = lazyListState
        ) {
            items(state.userList.size) { index ->
                PersonalJudgeCard(state.userList[index]) {
                    val userDetail = Json.encodeToString(UserDetail.serializer(), state.userList[index])
                    Log.e("TAG", "HomePage: $userDetail")
                    navHostController.navigate(Screen.UserDetailedPage.route + "/${userDetail}")
                }
            }
        }
    }
}