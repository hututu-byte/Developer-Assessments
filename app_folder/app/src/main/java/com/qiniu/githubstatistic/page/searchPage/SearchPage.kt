package com.qiniu.githubstatistic.page.searchPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.qiniu.githubstatistic.R
import com.qiniu.githubstatistic.customView.MajorTag
import com.qiniu.githubstatistic.navigation.Screen
import com.qiniu.githubstatistic.page.homePage.HomeIntent

@Composable
fun SearchPage(navHostController: NavHostController,viewModel: SearchViewModel = hiltViewModel()) {
    val tags = remember {mutableStateListOf(
        Pair("java",false),
        Pair("kotlin",false),
        Pair("python",false),
        Pair("c++",false),
        Pair("c",false),
        Pair("c#",false),
        Pair("javascript",false),
        Pair("typescript",false),
        Pair("html",false),
        Pair("css",false),
        Pair("php",false),
        Pair("shell",false),
        Pair("go",false),
        Pair("ruby",false),
        Pair("swift",false),
        Pair("rust",false),
        Pair("scala",false),
        Pair("perl",false),
        Pair("lua",false),
        Pair("r",false),
        Pair("matlab",false),
        Pair("objective-c",false),
        Pair("groovy",false),
        Pair("dart",false)
    ) }
    var currentMessage by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current //键盘
    val state by viewModel.searchState.collectAsState()
    if (state.chooseCountry) {
        AlertDialog(
            onDismissRequest = { viewModel.sendIntent(SearchPageIntent.DismissDialog) },
            title = { Text("Choose a country") },
            text = {
                // 使用 LazyColumn 列出国家选择项
                LazyColumn(Modifier.height(300.dp)) {
                    items(state.countryList.size) { index ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.sendIntent(SearchPageIntent.AddCountryConstrain(state.countryList[index]))
                                    viewModel.sendIntent(SearchPageIntent.DismissDialog)
                                }
                                .padding(8.dp)
                        ) {
                            Text(text = state.countryList[index], fontSize = 16.sp, modifier = Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            dismissButton = {
                Text(
                    text = "Cancel",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            viewModel.sendIntent(SearchPageIntent.DismissDialog)
                        }
                )
            },
            confirmButton = {
                Text(
                    text = "Confirm",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            viewModel.sendIntent(SearchPageIntent.DismissDialog)
                        }
                )
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Box(
            modifier = if (!state.isFocused) Modifier.fillMaxSize() else Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                AnimatedVisibility(!state.isFocused, enter = EnterTransition.None) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(R.drawable.github),
                            contentDescription = "github",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(64.dp)
                        )
                        Text(
                            "GithubStatistic",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 28.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {viewModel.sendIntent(SearchPageIntent.ChooseCountry) }, enabled = state.isFocused) {
                        Icon(
                            painter = painterResource(R.drawable.nation_search),
                            contentDescription = "nation_search",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    BasicTextField(
                        value = currentMessage,
                        onValueChange = { currentMessage = it },
                        textStyle = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .onFocusChanged {
                                if (it.isFocused) {
                                    viewModel.sendIntent(SearchPageIntent.ChangeFocus)
                                }
                            }
                            .width(260.dp)
                            .height(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .wrapContentHeight()
                            ) {
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (currentMessage.text.isEmpty()) {
                                        Text("Enter your message", color = Color.Gray)
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )

                    IconButton(onClick = {
                        keyboardController?.hide()
                        val searchKey = viewModel.getSearchKey()
                        viewModel.sendIntent(SearchPageIntent.Search)
                        navHostController.navigate(Screen.SearchResultPage.route + "/${searchKey}")
                    }, enabled = state.isFocused, modifier = Modifier.padding(start = 8.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.search),
                            contentDescription = "search"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                }

                AnimatedVisibility(visible = state.isFocused) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp), // 每个项的最小宽度为 100.dp
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tags.size) { index ->
                            MajorTag(tags[index].first, isSelected = tags[index].second, canSelect = true, select3 = state.usedTags.size >= 3, select = {
                                tags[index] = Pair(tags[index].first, !tags[index].second)
                                viewModel.sendIntent(SearchPageIntent.AddTags(tags[index].first, tags[index].second))
                            })
                        }
                    }
                }
            }
        }
    }

}