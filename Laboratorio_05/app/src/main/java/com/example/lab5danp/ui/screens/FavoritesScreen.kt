package com.example.lab5danp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab5danp.model.Product
import com.example.lab5danp.ui.components.AppToolbar
import com.example.lab5danp.ui.components.ProductCard

@Composable
fun FavoritesScreen(
    navController: NavController,
    favoriteProducts: List<Product>,
    onToggleFavorite: (Product) -> Unit
) {
    Column {
        AppToolbar(title = "Mis Favoritos")
        Spacer(modifier = Modifier.height(8.dp))

        if (favoriteProducts.isEmpty()) {
            Text(text = "No tienes favoritos aún.", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(favoriteProducts) { product ->
                    ProductCard(
                        product = product,
                        isFavorite = true,
                        onToggleFavorite = { onToggleFavorite(product) },
                        onViewDetail = { navController.navigate("detail/${it.name}/${it.price}/${it.description}/${it.imageRes}") }
                    )
                }
            }
        }
    }
}