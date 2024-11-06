package com.qiniu.githubstatistic.page.homePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.qiniu.githubstatistic.R
import com.qiniu.githubstatistic.customView.MajorTag
import com.qiniu.githubstatistic.model.UserDetail

@Composable
fun HomePage(homePageViewModel: HomePageViewModel = hiltViewModel()) {
    val state by homePageViewModel.homeState.collectAsState()
    val lazyListState = rememberLazyListState()

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
                PersonalJudgeCard(state.userList[it])
            }
        }
    }

    // 上拉加载更多功能
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                if (index == state.userList.lastIndex) {
                    // Call your load more logic here, e.g., viewModel.loadMoreData()
//                    homePageViewModel.loadMoreData()
                }
            }
    }
}


@Composable
@Preview
fun HomePagePreview() {
    HomePage()
}

@Composable
fun PersonalJudgeCard(userDetail: UserDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row (Modifier.fillMaxWidth()){
                Icon(
                    painter = painterResource(R.drawable.alienavatar),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(48.dp)
                )

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(text = userDetail.githubUsername, style = MaterialTheme.typography.bodyLarge, fontSize = 24.sp)
                    Text(
                        text = "From: ${userDetail.country}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.level),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(end = 8.dp).size(36.dp).align(Alignment.CenterVertically)
                )

                Text(userDetail.talentRank, style = MaterialTheme.typography.bodyMedium, fontSize = 18.sp, color = Color.Black, modifier = Modifier.align(Alignment.CenterVertically))


            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = userDetail.bio,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                maxLines = 3,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row {
                if (userDetail.mostCommonTag.isNotEmpty()) {
                    userDetail.mostCommonTag.split(",").forEach {
                        MajorTag(it)
                    }
                }

            }
        }
    }
}