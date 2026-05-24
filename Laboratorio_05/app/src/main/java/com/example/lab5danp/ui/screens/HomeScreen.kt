package com.example.lab5danp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab5danp.model.Product
import com.example.lab5danp.ui.components.AppButton
import com.example.lab5danp.ui.components.AppToolbar
import com.example.lab5danp.ui.components.CategoryCard
import com.example.lab5danp.ui.components.ProductCard
import com.example.lab5danp.ui.components.ThemeSelector

@Composable
fun HomeScreen(
    navController: NavController,
    favoriteProducts: List<Product>,
    cartProducts: List<Product>, // Añadido
    onToggleFavorite: (Product) -> Unit,
    onToggleCart: (Product) -> Unit, // Añadido
    onThemeChange: (String) -> Unit
) {
    val products = remember {
        listOf(
            Product(1, "Laptop Gamer", "RTX 4070 + Ryzen 9", 2500.0),
            Product(2, "Mechanical Keyboard", "RGB Switch Blue", 120.0),
            Product(3, "Gaming Mouse", "16000 DPI", 75.0)
        )
    }

    Column {
        AppToolbar(title = "Modular Store")
        Spacer(modifier = Modifier.height(12.dp))

        ThemeSelector { onThemeChange(it.name) }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Categorías",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            CategoryCard(categoryName = "Computadoras", modifier = Modifier.weight(1f), onClick = { })
            CategoryCard(categoryName = "Periféricos", modifier = Modifier.weight(1f), onClick = { })
        }

        Spacer(modifier = Modifier.height(8.dp))

        // FILA DE ACCESOS CON CONTADORES DINÁMICOS
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AppButton(
                text = "Favs (${favoriteProducts.size})",
                onClick = { navController.navigate("favorites") }
            )
            AppButton(
                text = "Carrito (${cartProducts.size})",
                onClick = { navController.navigate("cart") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(products) { product ->
                ProductCard(
                    product = product,
                    isFavorite = favoriteProducts.contains(product),
                    isInCart = cartProducts.contains(product),
                    onToggleFavorite = { onToggleFavorite(product) },
                    onAddToCart = { onToggleCart(product) },
                    onViewDetail = { navController.navigate("detail/${it.name}/${it.price}") }
                )
            }
        }
    }
}