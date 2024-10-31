package com.qiniu.githubstatistic.page.userDetailedPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qiniu.githubstatistic.R
import com.qiniu.githubstatistic.ui.theme.GithubStatisticTheme
import com.qiniu.githubstatistic.ui.theme.LocalCustomColors

@Composable
@Preview
fun UserDetailedPage() {
    val customColors = LocalCustomColors.current
    Column (modifier = Modifier
        .fillMaxSize()
        .background(customColors.onBackground)
        .statusBarsPadding()){
        Column {
            Icon(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(16.dp)
                    .size(36.dp)
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
                    text = "User Name",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = 28.sp
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.follower),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(start = 18.dp, end = 9.dp)
                        .padding(vertical = 18.dp)
                        .size(36.dp)
                )
                Text(" 4 followers· 4 following", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.CenterVertically), fontSize = 18.sp)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.nation),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(start = 18.dp, end = 9.dp)
                        .padding(vertical = 9.dp)
                        .size(36.dp)
                )
                Text(" China", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.CenterVertically), fontSize = 18.sp)
            }
            StatisticCard(customColors.onBackground)
        }



    }
}

@Composable
fun StatisticCard(color: Color){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .height(200.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().background(color = color).padding(16.dp)) {
            Text("OverView", style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}




