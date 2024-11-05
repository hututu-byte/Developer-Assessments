package com.qiniu.githubstatistic.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
//    background = DarkModeColor.BackGroundColor,
//    surface = DarkModeColor.CardColor,
)

data class CustomLightColorScheme(
    val colorScheme: ColorScheme,
    val tagColor: Color,
)

data class CustomDarkColorScheme(
    val colorScheme: ColorScheme,
    val tagColor: Color,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
//    background = LightModeColor.BackGroundColor,
//    surface = LightModeColor.CardColor,
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
    /***
    /* 主色系 */
    primary: Color, // 主色，用于应用的大部分 UI 元素，如按钮、选中的选项卡等。
    onPrimary: Color, // 在主色上清晰显示的颜色，通常用于文本或图标。
    primaryContainer: Color, // 主色的容器色，用于需要主色变体的元素背景。
    onPrimaryContainer: Color, // 在主色容器上清晰显示的颜色，通常用于文本或图标。
    inversePrimary: Color, // 主色的反色，用于在对比背景上需要主色时。

    /* 次色系 */
    secondary: Color, // 次级色，用于补充主色或用作次要的 UI 元素。
    onSecondary: Color, // 在次级色上清晰显示的颜色，通常用于文本或图标。
    secondaryContainer: Color, // 次级色的容器色，用于需要次级色变体的元素背景。
    onSecondaryContainer: Color, // 在次级色容器上清晰显示的颜色，通常用于文本或图标。

    /* 第三色系 */
    tertiary: Color, // 第三色，用于需要注意或区分的 UI 元素。
    onTertiary: Color, // 在第三色上清晰显示的颜色，通常用于文本或图标。
    tertiaryContainer: Color, // 第三色的容器色，用于背景或填充色。
    onTertiaryContainer: Color, // 在第三色容器上清晰显示的颜色，通常用于文本或图标。

    /* 背景与表面色 */
    background: Color, // 背景色，用于页面或组件的背景。
    onBackground: Color, // 在背景色上清晰显示的颜色，通常用于文本或图标。
    surface: Color, // 表面色，用于卡片、菜单和其他元素的背景。
    onSurface: Color, // 在表面色上清晰显示的颜色，通常用于文本或图标。
    surfaceVariant: Color, // 表面色的变体，用于需要区分的表面元素。
    onSurfaceVariant: Color, // 在表面色变体上清晰显示的颜色。
    surfaceTint: Color, // 表面色的着色，通常用于表面元素的图标或小组件。
    inverseSurface: Color, // 表面色的反色，用于需要高对比度的背景。
    inverseOnSurface: Color, // 在反表面色上清晰显示的颜色。

    /* 错误处理色 */
    error: Color, // 错误色，用于指示错误或警告状态，如输入校验失败。
    onError: Color, // 在错误色上清晰显示的颜色，通常用于错误文本或图标。
    errorContainer: Color, // 错误色的容器色，用于错误状态的背景。
    onErrorContainer: Color, // 在错误容器色上清晰显示的颜色。

    /* 其他 */
    outline: Color, // 用于元素边框的颜色。
    outlineVariant: Color, // 边框颜色的变体，可能用于更细微的分界线。
    scrim: Color, // 遮罩层颜色，通常用于遮盖或暗化背景中的内容。
     ***/
)

// 定义自定义颜色方案的数据类
data class CustomColorScheme(
    val colorScheme: ColorScheme,
    val tagColor: Color,
    val tagColorSelected: Color,
    val onBackground: Color
)

// 定义 CompositionLocal
val LocalCustomColors = compositionLocalOf {
    CustomColorScheme(
        lightColorScheme(),
        TagColorLight,
        TagColorLightSelected,
        OnBackgroundLight
    )
}

@Composable
fun GithubStatisticTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
    val tagColor = if (darkTheme) TagColorDark else TagColorLight
    val tagColorSelected = if (darkTheme) TagColorDarkSelected else TagColorLightSelected
    val onBackground = if (darkTheme) OnBackgroundDark else OnBackgroundLight

    val extendedColors = CustomColorScheme(
        colorScheme = colorScheme,
        tagColor = tagColor,
        tagColorSelected = tagColorSelected,
        onBackground = onBackground
    )

    CompositionLocalProvider(LocalCustomColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}