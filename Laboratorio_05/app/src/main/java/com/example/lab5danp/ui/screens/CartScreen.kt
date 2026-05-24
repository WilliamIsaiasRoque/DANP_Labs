package com.example.lab5danp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab5danp.model.Product
import com.example.lab5danp.ui.components.AppButton
import com.example.lab5danp.ui.components.AppToolbar
import com.example.lab5danp.ui.components.ProductCard

@Composable
fun CartScreen(
    navController: NavController,
    cartProducts: List<Product>,
    favoriteProducts: List<Product>,
    onRemoveFromCart: (Product) -> Unit,
    onToggleFavorite: (Product) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppToolbar(title = "Mi Carrito de Compras")
        Spacer(modifier = Modifier.height(8.dp))

        if (cartProducts.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(text = "El carrito está vacío.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartProducts) { product ->
                    ProductCard(
                        product = product,
                        isFavorite = favoriteProducts.contains(product),
                        isInCart = true,
                        onToggleFavorite = { onToggleFavorite(product) },
                        onAddToCart = { onRemoveFromCart(product) },
                        onViewDetail = { navController.navigate("detail/${it.name}/${it.price}/${it.description}/${it.imageRes}") }
                    )
                }
            }

            val total = cartProducts.sumOf { it.price }
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Total a Pagar: $$total", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(12.dp))
                AppButton(text = "Finalizar Compra", onClick = { })
            }
        }
    }
}