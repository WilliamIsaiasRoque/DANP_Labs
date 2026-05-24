package com.example.lab5danp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab5danp.model.Product
import com.example.lab5danp.ui.screens.CartScreen
import com.example.lab5danp.ui.screens.DetailScreen
import com.example.lab5danp.ui.screens.FavoritesScreen
import com.example.lab5danp.ui.screens.HomeScreen

@Composable
fun AppNavigation(
    onThemeChange: (String) -> Unit
) {
    val navController = rememberNavController()
    val favoriteProducts = remember { mutableStateListOf<Product>() }
    val cartProducts = remember { mutableStateListOf<Product>() }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                navController = navController,
                favoriteProducts = favoriteProducts,
                cartProducts = cartProducts,
                onToggleFavorite = { product ->
                    if (favoriteProducts.contains(product)) favoriteProducts.remove(product)
                    else favoriteProducts.add(product)
                },
                onToggleCart = { product ->
                    if (cartProducts.contains(product)) cartProducts.remove(product)
                    else cartProducts.add(product)
                },
                onThemeChange = onThemeChange
            )
        }

        composable("favorites") {
            FavoritesScreen(
                navController = navController,
                favoriteProducts = favoriteProducts,
                onToggleFavorite = { favoriteProducts.remove(it) }
            )
        }

        composable("cart") {
            CartScreen(
                navController = navController,
                cartProducts = cartProducts,
                favoriteProducts = favoriteProducts,
                onRemoveFromCart = { cartProducts.remove(it) },
                onToggleFavorite = { product ->
                    if (favoriteProducts.contains(product)) favoriteProducts.remove(product)
                    else favoriteProducts.add(product)
                }
            )
        }

        // ruta final
        composable("detail/{name}/{price}/{description}/{imageRes}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val price = backStackEntry.arguments?.getString("price") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val imageRes = backStackEntry.arguments?.getString("imageRes")?.toIntOrNull() ?: 0

            DetailScreen(
                navController = navController,
                name = name,
                price = price,
                description = description,
                imageRes = imageRes // Mandamos el entero de la imagen
            )
        }
    }
}