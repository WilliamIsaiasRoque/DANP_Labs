package com.example.lab5danp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab5danp.ui.components.AppButton
import com.example.lab5danp.ui.components.AppToolbar

@Composable
fun DetailScreen(
    navController: NavController,
    name: String,
    price: String,
    description: String,
    imageRes: Int // ID de la imagen
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppToolbar(title = "Detalle del Producto")

        //  renderizar imagen
        if (imageRes != 0) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Imagen del producto: $name",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop // Evita que la imagen se deforme
            )
        } else {
            // Espacio de respaldo por si no carga la imagen
            Spacer(modifier = Modifier.height(240.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Precio: $$price",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Descripción del producto:",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = "Volver a la Tienda",
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.popBackStack() }
            )
        }
    }
}