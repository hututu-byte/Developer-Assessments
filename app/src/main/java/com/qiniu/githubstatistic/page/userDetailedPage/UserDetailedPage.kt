package com.qiniu.githubstatistic.page.userDetailedPage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.qiniu.githubstatistic.R
import com.qiniu.githubstatistic.model.Repository
import com.qiniu.githubstatistic.model.UserDetail
import com.qiniu.githubstatistic.ui.theme.LanguageColor
import com.qiniu.githubstatistic.ui.theme.LocalCustomColors
import kotlinx.serialization.json.Json

@Composable
fun UserDetailedPage(userDetail: String, navHostController: NavHostController) {
    val customColors = LocalCustomColors.current
    val userDetail = Json.decodeFromString(UserDetail.serializer(), userDetail)
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
                    fontSize = 28.sp
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.follower),
                    contentDescription = null,
                    modifier = Modifier
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
            StatisticCard(customColors.userPageBackground)
        }


    }
}

@Composable
fun StatisticCard(color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .height(200.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color)
                .padding(16.dp)
        ) {
            Text(
                "OverView",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun RepositoryCard(repository: Repository) {

    Box(
        modifier = Modifier
            .width(250.dp)
            .height(130.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                "TikTok",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
            )
            Text(
                "仿抖音的一个Android App",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
            )
            Spacer(Modifier.weight(1f))
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(16.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    "4",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(10.dp)
                        .background(
                            color = if (LanguageColor.colorMap["Java"] != null) LanguageColor.colorMap["Java"]!! else Color.Gray,
                            shape = CircleShape
                        )
                        .align(Alignment.CenterVertically)
                )

                Text(
                    "Java",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                )
            }
        }
    }

}




