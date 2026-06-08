package com.example.lab7danp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab7danp.ui.screens.AddEditScreen
import com.example.lab7danp.ui.screens.CatalogScreen
import com.example.lab7danp.viewmodel.ProductViewModel

@Composable
fun AppNavigation(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "catalog") {
        composable("catalog") {
            CatalogScreen(viewModel = viewModel, navController = navController)
        }
        composable("add_edit/{productId}") { backStackEntry ->
            // Recuperamos el ID pasado por la ruta para saber si creamos o editamos
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: 0
            AddEditScreen(productId = productId, viewModel = viewModel, navController = navController)
        }
    }
}