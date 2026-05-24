package com.example.lab5danp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab5danp.ui.components.AppToolbar

@Composable
fun DetailScreen(
    name: String,
    price: String
) {
    Column {
        AppToolbar(title = "Detalle")
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Precio: $$price",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}