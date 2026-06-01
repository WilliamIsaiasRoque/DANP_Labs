package com.example.lab6danp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab6danp.ui.screens.DetailScreen
import com.example.lab6danp.ui.screens.MenuScreen
import com.example.lab6danp.viewmodel.MenuViewModel

@Composable
fun AppNavigation(viewModel: MenuViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(viewModel = viewModel, navController = navController)
        }
        composable("detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0
            DetailScreen(itemId = itemId, viewModel = viewModel, navController = navController)
        }
    }
}