package com.qiniu.githubstatistic.customView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qiniu.githubstatistic.ui.theme.LocalCustomColors

@Composable
fun MajorTag(
    major: String,
    isSelected: Boolean = false,
    canSelect: Boolean = false,
    select: () -> Unit = {},
    select3: Boolean = false
) {
    val customColors = LocalCustomColors.current
    var tagModifier =
        if (major.length > 7 && !canSelect) Modifier
            .padding(horizontal = 8.dp)
            .wrapContentWidth()
            .height(28.dp)
        else if (!canSelect) Modifier
            .padding(horizontal = 8.dp)
            .width(76.dp)
            .height(28.dp)
        else Modifier
            .fillMaxWidth()
            .height(28.dp)
    if (canSelect && !select3 || isSelected)
        tagModifier = tagModifier.clickable { select() }
    Box(
        Modifier
            .padding(horizontal = 8.dp)
            .background(
                color = if (!isSelected) customColors.tagColor else customColors.tagColorSelected,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = tagModifier
                .background(
                    color = if (!isSelected) customColors.tagColor else customColors.tagColorSelected,
                    shape = RoundedCornerShape(8.dp)
                )
                .align(Alignment.Center)
        ) {
            Text(
                text = major,
                style = MaterialTheme.typography.bodySmall,
                color = customColors.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}