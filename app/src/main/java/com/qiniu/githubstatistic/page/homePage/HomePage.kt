package com.qiniu.githubstatistic.page.homePage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.qiniu.githubstatistic.R
import com.qiniu.githubstatistic.customView.MajorTag
import com.qiniu.githubstatistic.model.UserDetail
import com.qiniu.githubstatistic.navigation.Screen
import kotlinx.serialization.json.Json

@Composable
fun HomePage(navHostController: NavHostController,homePageViewModel: HomePageViewModel = hiltViewModel()) {
    val state by homePageViewModel.homeState.collectAsState()
    val lazyListState = rememberLazyListState()

    if (state.showDialog) {
        AlertDialog(
            onDismissRequest = { homePageViewModel.sendIntent(HomeIntent.Refresh) },
            title = { Text("Error") },
            text = { Text(state.error) },
            dismissButton = {
                Text(
                    text = "Cancel",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            homePageViewModel.sendIntent(HomeIntent.DismissDialog)
                        }
                )
            },
            confirmButton = {
                Text(
                    text = "Retry",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            homePageViewModel.sendIntent(HomeIntent.DismissDialog)
                            homePageViewModel.sendIntent(HomeIntent.Refresh)
                        }
                )
            }
        )
    }

    // 下拉刷新功能
    SwipeRefresh(
        state = rememberSwipeRefreshState(state.refreshing),
        onRefresh = {
            homePageViewModel.sendIntent(HomeIntent.Refresh)
        },
        modifier = Modifier.statusBarsPadding(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            state = lazyListState
        ) {
            items(state.userList.size) {
                PersonalJudgeCard(state.userList[it]){
                    val userDetail = Json.encodeToString(UserDetail.serializer(),state.userList[it])
                    Log.e("TAG", "HomePage: $userDetail", )
                    navHostController.navigate(Screen.UserDetailedPage.route + "/${userDetail}")
                }
            }
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo }
            .collect { layoutInfo ->
                val visibleItems = layoutInfo.visibleItemsInfo
                if (visibleItems.isNotEmpty()) {
                    val lastVisibleItemIndex = visibleItems.last().index
                    val totalItems = state.userList.size
                    // 检查是否接近列表底部
                    if (lastVisibleItemIndex == totalItems - 1 && (lastVisibleItemIndex+1) % 10 == 0 && !state.loading) {
                        homePageViewModel.sendIntent(HomeIntent.Loading)
                    }
                }
            }
    }
}

@Composable
fun PersonalJudgeCard(userDetail: UserDetail,more:()->Unit = {}) {
    if (userDetail.country.length > 20){
        val list = userDetail.country.split(",")
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surface)
            .clickable { more() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.alienavatar),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(48.dp)
                )

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = userDetail.githubUsername,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "From: ${userDetail.country}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp
                    )
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (userDetail.bio == "null") "The man is lazy,there is no bio" else userDetail.bio,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                maxLines = 3,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row {
                if (userDetail.mostCommonTag.isNotEmpty()) {
                    userDetail.mostCommonTag.split(",").forEach {
                        if (!it.contains("null"))
                            MajorTag(it)
                    }
                }
            }

            Row (modifier = Modifier.padding(top = 8.dp)){
                Icon(
                    painter = painterResource(R.drawable.level),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(36.dp)
                        .align(Alignment.CenterVertically)
                )

                Text(
                    userDetail.talentRank,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

        }
    }
}