package com.qiniu.githubstatistic.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambdaN
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qiniu.githubstatistic.R
import com.qiniu.githubstatistic.page.homePage.HomePage
import com.qiniu.githubstatistic.page.searchPage.SearchPage
import com.qiniu.githubstatistic.page.userDetailedPage.UserDetailedPage

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    innerPaddingValues: PaddingValues,
    startDestination: String = Screen.HomePage.route,
){
    var select by remember {  mutableIntStateOf(0) }
    var isNavBarVis by remember { mutableStateOf(false) }
//    LaunchedEffect(navHostController.currentDestination?.route) {
//        println( navHostController.currentDestination?.route)
//        when(navHostController.currentDestination?.route){
//            Screen.HomePage.route -> select = 0
//            Screen.SearchPage.route -> select = 1
//        }
//    }
    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navHostController, startDestination = startDestination,modifier = Modifier.weight(1f)){
            composable(Screen.HomePage.route){
                HomePage()
            }
            composable(Screen.UserDetailedPage.route){
                UserDetailedPage()
            }
            composable(Screen.SearchPage.route){
                SearchPage()
            }
//        composable(Screen.ModelPage.route){
//            ModelPage(navHostController)
//        }
//        composable(
//            route = Screen.ChatPage.route + "/{model}" + "/{id}",
//            arguments = listOf(navArgument("model") { type = NavType.StringType }, navArgument("id"){type = NavType.IntType})
//        ) {
//            val model = it.arguments?.getString("model") ?: "GPT-4o"
//            val chatId = it.arguments?.getInt("id")?:0
//            ChatPage(navHostController, model,chatId)
//        }
        }
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp).clickable {
                if (select != 0) {
                    navHostController.navigate(Screen.HomePage.route) {
                        popUpTo(Screen.HomePage.route) {
                            select = 0
                            inclusive = true
                        }
                    }
                }
            }) {
                Icon(
                    painterResource(id = if (select == 0) R.drawable.home_fill else R.drawable.home),
                    contentDescription = "Previous Level",
                    modifier = Modifier
                        .size(28.dp).align(Alignment.CenterHorizontally),
                    tint = Color.Unspecified
                )
                Text(text = "首页", fontSize = 16.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            Column(modifier = Modifier.padding(vertical = 4.dp).clickable {
                if (select != 1) {
                    select = 1
                    navHostController.navigate(Screen.SearchPage.route) {
                        popUpTo(Screen.SearchPage.route) {
                            inclusive = true
                        }
                    }
                }
            }) {
                Icon(
                    painterResource(id = if (select == 1) R.drawable.search_filled else R.drawable.search_outline),
                    contentDescription = "我的",
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.CenterHorizontally),
                    tint = Color.Unspecified
                )
                Text(text = "搜索", fontSize = 16.sp,modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }

}