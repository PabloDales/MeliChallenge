package com.example.melichallenge.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.melichallenge.screens.ProductDetailScreen
import com.example.melichallenge.screens.SearchResultScreen
import com.example.melichallenge.screens.SearchScreen
import com.example.melichallenge.screens.SearchScreenViewModel
import com.example.melichallenge.screens.SearchViewModel


@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val searchViewModel: SearchViewModel = hiltViewModel()
    val searchScreenViewModel: SearchScreenViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            SearchResultScreen(
                navController = navController,
                viewModel = searchScreenViewModel
            )
        }

        composable(Screen.SearchHistory.route) {
            SearchScreen(
                navController = navController,
                searchViewModel = searchViewModel,
                searchScreenViewModel = searchScreenViewModel
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) {
            ProductDetailScreen(
                navController = navController
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object SearchHistory : Screen("search_history")
    data object Detail : Screen("detail/{productId}") {
        fun createRoute(productId: String) = "detail/$productId"
    }
}