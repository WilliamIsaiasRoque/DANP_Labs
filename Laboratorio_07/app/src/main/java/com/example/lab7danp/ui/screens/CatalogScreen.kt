package com.example.lab7danp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab7danp.model.ProductUiState
import com.example.lab7danp.ui.components.ProductItem
import com.example.lab7danp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(viewModel: ProductViewModel, navController: NavController) {
    // Estado y Recomposición
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inventario DANP") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_edit/0") }) { // ID 0 indica "Nuevo"
                Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchProducts(it) // Dispara la búsqueda al instante
                },
                label = { Text("Buscar por nombre o categoría...") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            when (uiState) {
                is ProductUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ProductUiState.Error -> {
                    Text("Error al cargar datos", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                }
                is ProductUiState.Success -> {
                    val products = (uiState as ProductUiState.Success).products
                    // Cumple Requisito: Optimización de Listas
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(products, key = { it.id }) { product ->
                            ProductItem(
                                product = product,
                                onEdit = { navController.navigate("add_edit/${product.id}") },
                                onDelete = { viewModel.deleteProduct(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}