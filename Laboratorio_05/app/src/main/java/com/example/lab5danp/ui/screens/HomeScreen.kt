package com.example.lab5danp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab5danp.model.Product
import com.example.lab5danp.ui.components.AppToolbar
import com.example.lab5danp.ui.components.ProductCard
import com.example.lab5danp.ui.components.ThemeSelector

@Composable
fun HomeScreen(
    navController: NavController,
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
        ThemeSelector {
            onThemeChange(it.name)
        }

        LazyColumn {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onViewDetail = {
                        navController.navigate("detail/${it.name}/${it.price}")
                    }
                )
            }
        }
    }
}