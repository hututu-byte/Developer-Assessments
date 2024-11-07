package com.qiniu.githubstatistic.customView

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qiniu.githubstatistic.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LoadingAnimation(isCompleted: Boolean = false, onAnimEnded: () -> Unit = {}) {
    val rotation = remember { Animatable(0f) }
    val translationX = remember { Animatable(0f) }
    val animationDuration = if (isCompleted) 1000 else 6000
    val easing = if (isCompleted) FastOutSlowInEasing else LinearEasing

    // 协同启动旋转和平移动画
    LaunchedEffect(isCompleted) {
        launch {
            rotation.animateTo(
                targetValue = 360f,
                animationSpec = tween(durationMillis = animationDuration, easing = easing)
            )
        }
        launch {
            translationX.animateTo(
                targetValue = 800f,
                animationSpec = tween(durationMillis = animationDuration, easing = easing)
            )
        }
    }

    // 监测动画结束
    LaunchedEffect(translationX.value) {
        if (translationX.value >= 799.9f) {
            onAnimEnded()
        }
    }

    // 更新UI
    val progress = translationX.value / 800f
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.github_loading),
                contentDescription = "Github",
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(100.dp)
                    .graphicsLayer(
                        rotationZ = rotation.value,
                        translationX = translationX.value
                    )
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(4.dp),
        )
    }
}


