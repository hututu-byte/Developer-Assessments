package com.qiniu.githubstatistic.customView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qiniu.githubstatistic.ui.theme.GithubStatisticTheme
import com.qiniu.githubstatistic.ui.theme.LocalCustomColors

@Composable
fun MajorTag(major:String) {
    val customColors = LocalCustomColors.current
    Box(modifier = Modifier
        .padding(horizontal = 8.dp)
        .height(24.dp)
        .width(64.dp)
        .background(color = customColors.tagColor, shape = RoundedCornerShape(8.dp))
        ){
        Text(
            text = major,
            style = MaterialTheme.typography.bodySmall,
            color = customColors.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}