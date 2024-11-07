package com.qiniu.githubstatistic.page.searchResultPage

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.qiniu.githubstatistic.customView.LoadingAnimation
import com.qiniu.githubstatistic.model.UserDetail
import com.qiniu.githubstatistic.navigation.Screen
import com.qiniu.githubstatistic.page.homePage.HomeIntent
import com.qiniu.githubstatistic.page.homePage.PersonalJudgeCard
import kotlinx.serialization.json.Json

@Composable
fun SearchResultsPage(navHostController:NavHostController,searchKey:String,viewModel: SearchResultViewModel = hiltViewModel()) {
    val state by viewModel.searchResultPageState.collectAsState()
    LaunchedEffect (searchKey){
        if (!state.hasSearched)
            viewModel.sendIntent(SearchResultIntent.Search(searchKey))
    }
    val lazyListState = rememberLazyListState()

    if (state.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.sendIntent(SearchResultIntent.DismissDialog) },
            title = { Text("Error") },
            text = { Text(state.error) },
            dismissButton = {
                Text(
                    text = "Cancel",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            viewModel.sendIntent(SearchResultIntent.DismissDialog)
                            navHostController.popBackStack()
                        }
                )
            },
            confirmButton = {
                Text(
                    text = "Retry",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            viewModel.sendIntent(SearchResultIntent.DismissDialog)
                            viewModel.sendIntent(SearchResultIntent.Search(searchKey))
                        }
                )
            }
        )
    }

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