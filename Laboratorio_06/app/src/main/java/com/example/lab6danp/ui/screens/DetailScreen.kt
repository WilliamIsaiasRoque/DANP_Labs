package com.example.lab6danp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab6danp.model.Coffee
import com.example.lab6danp.model.Dessert
import com.example.lab6danp.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(itemId: Int, viewModel: MenuViewModel, navController: NavController) {
    val item = viewModel.getItem(itemId)
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { paddingValues ->
        if (item != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Imagen grande
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = item.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = item.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "S/ ${item.price}", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.secondary)

                Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

                // Botón inteligente (Aplica ISP chequeando qué tipo de producto es)
                Button(
                    onClick = { Toast.makeText(context, "¡Has pedido ${item.name}!", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    when (item) {
                        is Coffee -> Text(if (item.isHot) "Pedir Caliente" else "Pedir Helado")
                        is Dessert -> Text(if (item.isVegan) "Pedir (Vegano)" else "Pedir Postre")
                        else -> Text("Añadir al carrito")
                    }
                }
            }
        }
    }
}