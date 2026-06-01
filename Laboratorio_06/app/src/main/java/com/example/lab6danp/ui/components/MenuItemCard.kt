package com.example.lab6danp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab6danp.model.MenuItem

// OCP
// Componente cerrado a la modificación, pero abierto a la extensión (recibe un slot inyectable).
@Composable
fun MenuItemCard(
    item: MenuItem,
    actionSlot: @Composable () -> Unit // Slot para extender la UI
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "S/ ${item.price}", color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(12.dp))

            // Renderiza el botón o interfaz extendida que le inyecten
            actionSlot()
        }
    }
}