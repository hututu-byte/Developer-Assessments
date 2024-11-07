package com.qiniu.githubstatistic.page.userDetailedPage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.qiniu.githubstatistic.R
import com.qiniu.githubstatistic.model.Repository
import com.qiniu.githubstatistic.model.UserDetail
import com.qiniu.githubstatistic.model.UserDetailMore
import com.qiniu.githubstatistic.ui.theme.LanguageColor
import com.qiniu.githubstatistic.ui.theme.LocalCustomColors
import kotlinx.serialization.json.Json

@Composable
fun UserDetailedPage(userDetail: String, navHostController: NavHostController,viewModel:UserDetailViewModel = hiltViewModel()) {
    val customColors = LocalCustomColors.current
    val userDetail = Json.decodeFromString(UserDetail.serializer(), userDetail)
    val userDetailMore = viewModel.userDetailMore.collectAsState()
    LaunchedEffect(true) {
        viewModel.getUserDetailMore(userDetail.githubId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.userPageBackground)
            .statusBarsPadding()
    ) {
        Column {
            Icon(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(36.dp)
                    .clickable {
                        navHostController.popBackStack()
                    }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.photo),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(18.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = userDetail.githubUsername,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = 24.sp
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.follower),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 18.dp, end = 9.dp)
                        .padding(vertical = 18.dp)
                        .size(36.dp)
                )
                Text(
                    " ${userDetail.followers} followers· ${userDetail.following} following",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = 18.sp
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.nation),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 18.dp, end = 9.dp)
                        .padding(vertical = 9.dp)
                        .size(36.dp)
                )
                Text(
                    " ${userDetail.country}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = 18.sp
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.star),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterVertically)
                        .padding(start = 18.dp, end = 9.dp)
                        .padding(vertical = 9.dp)
                        .size(36.dp)
                )
                Text(
                    " ${userDetailMore.value.totalStars} stars",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = 18.sp
                )
                Icon(
                    painter = painterResource(R.drawable.fork),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterVertically)
                        .padding(start = 18.dp, end = 9.dp)
                        .padding(vertical = 9.dp)
                        .size(36.dp)
                )
                Text(
                    " ${userDetailMore.value.totalForks} forks",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = 18.sp
                )
            }
            ProjectsCard(userDetailMore.value,customColors.userPageBackground,customColors.repositoryBorder)
        }


    }
}

@Composable
fun ProjectsCard(userDetailMore: UserDetailMore?, backColor: Color, borderColor:Color) {
    println(userDetailMore)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backColor)
                .padding(16.dp)
        ) {
            Text(
                "Hot Projects",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyRow(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (userDetailMore != null) {
                    items(userDetailMore.topProjects.size) { index ->
                        RepositoryCard(userDetailMore.topProjects[index],borderColor)
                    }
                }
            }
        }
    }
}

@Composable
fun RepositoryCard(repository: Repository,borderColor:Color) {

    Box(
        modifier = Modifier
            .width(250.dp)
            .height(150.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                repository.name,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier,
                maxLines = 1
            )
            Text(
                repository.description,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 6.dp, bottom = 12.dp),
                maxLines = 2
            )
            Spacer(Modifier.weight(1f))
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                        .size(16.dp)
                )
                Text(
                    repository.stars.toString(),
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                        .size(8.dp)
                        .background(
                            color = if (LanguageColor.colorMap[repository.language] != null) LanguageColor.colorMap[repository.language]!! else Color.Gray,
                            shape = CircleShape
                        )

                )

                Text(
                    repository.language,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                )
            }
        }
    }
}




