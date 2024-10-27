package com.qiniu.githubstatistic.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.qiniu.githubstatistic.page.homePage.HomePage

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    innerPaddingValues: PaddingValues,
    startDestination: String = Screen.HomePage.route,
){

    NavHost(navController = navHostController, startDestination = startDestination){
        composable(Screen.HomePage.route){
            HomePage()
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
}