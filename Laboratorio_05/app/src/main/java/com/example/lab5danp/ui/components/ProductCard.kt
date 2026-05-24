package com.example.lab5danp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab5danp.model.Product

@Composable
fun ProductCard(
    product: Product,
    onViewDetail: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.description)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$${product.price}")
            Spacer(modifier = Modifier.height(12.dp))
            AppButton(
                text = "Ver detalle",
                onClick = { onViewDetail(product) }
            )
        }
    }
}