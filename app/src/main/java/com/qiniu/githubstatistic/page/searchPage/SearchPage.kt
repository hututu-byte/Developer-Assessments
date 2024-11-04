package com.qiniu.githubstatistic.page.searchPage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import com.qiniu.githubstatistic.R

@Composable
fun SearchPage(viewModel: SearchViewModel = hiltViewModel())
{
    var currentMessage by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current //键盘
    val state = viewModel.searchState.collectAsState()
    var isFocused by remember { mutableStateOf(false) }


    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
        Box(modifier = if (!isFocused) Modifier.fillMaxSize() else Modifier.wrapContentHeight().fillMaxWidth()) {
            Row (modifier = Modifier.padding(16.dp).align(Alignment.Center)){
                BasicTextField(
                    value = currentMessage,
                    onValueChange = { currentMessage = it },
                    modifier = Modifier
                        .onFocusChanged {
                             isFocused = it.isFocused
                        }
                        .width(250.dp)
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

                }, modifier = Modifier.padding(horizontal = 16.dp)) {
                    Icon(painter = painterResource(R.drawable.search), contentDescription = "search")
                }

            }
        }
    }

}