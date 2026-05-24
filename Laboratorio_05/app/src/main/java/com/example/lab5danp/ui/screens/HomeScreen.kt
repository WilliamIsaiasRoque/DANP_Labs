package com.example.lab5danp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab5danp.R
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
    cartProducts: List<Product>,
    onToggleFavorite: (Product) -> Unit,
    onToggleCart: (Product) -> Unit,
    onThemeChange: (String) -> Unit
) {
    val products = remember {
        listOf(
            Product(1, "Laptop Gamer", "RTX 4070 + Ryzen 9, ideal para desarrollo y renderizado", 2500.0, R.drawable.laptop),
            Product(2, "Mechanical Keyboard", "RGB Switch Blue con retroalimentación táctil", 120.0, R.drawable.keyboard),
            Product(3, "Gaming Mouse", "16000 DPI con sensor óptico de alta precisión", 75.0, R.drawable.mouse),
            Product(4, "Monitor 4K Pro", "Pantalla de 32 pulgadas IPS a 144Hz para diseño profesional", 450.0, R.drawable.monitor)
        )
    }

    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = remember(searchQuery) {
        products.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column {
        AppToolbar(title = "Modular Store")
        Spacer(modifier = Modifier.height(12.dp))

        ThemeSelector { onThemeChange(it.name) }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Buscar producto...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Categorías", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            CategoryCard(categoryName = "Computadoras", modifier = Modifier.weight(1f), onClick = { })
            CategoryCard(categoryName = "Periféricos", modifier = Modifier.weight(1f), onClick = { })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AppButton(text = "Favs (${favoriteProducts.size})", onClick = { navController.navigate("favorites") })
            AppButton(text = "Carrito (${cartProducts.size})", onClick = { navController.navigate("cart") })
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(filteredProducts) { product ->
                ProductCard(
                    product = product,
                    isFavorite = favoriteProducts.contains(product),
                    isInCart = cartProducts.contains(product),
                    onToggleFavorite = { onToggleFavorite(product) },
                    onAddToCart = { onToggleCart(product) },
                    // pasar el id de la ruta de la imagen
                    onViewDetail = { navController.navigate("detail/${it.name}/${it.price}/${it.description}/${it.imageRes}") }
                )
            }
        }
    }
}