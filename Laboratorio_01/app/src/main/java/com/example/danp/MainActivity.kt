package com.example.danp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = TareaDatabase.getDatabase(applicationContext)
        val viewModel: TareasViewModel by viewModels { TareasViewModelFactory(db.tareaDao()) }

        setContent {
            MaterialTheme {
                AppTareas(viewModel)
            }
        }
    }
}

@Composable
fun AppTareas(viewModel: TareasViewModel) {
    val tareas by viewModel.todasLasTareas.collectAsState(initial = emptyList())

    var textoInput by remember { mutableStateOf("") }
    var tareaParaEdicion by remember { mutableStateOf<Tarea?>(null) }
    var nuevoTextoEdicion by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TituloApp()
        Spacer(modifier = Modifier.height(16.dp))

        CampoTexto(
            valor = textoInput,
            onValorChange = { textoInput = it },
            label = "Nueva tarea"
        )

        Spacer(modifier = Modifier.height(8.dp))

        BotonPrimario(texto = "Agregar tarea") {
            if (textoInput.isNotBlank()) {
                viewModel.agregarTarea(textoInput)
                textoInput = ""
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ListaTareas(
            tareas = tareas,
            onToggle = { viewModel.toggleTarea(it) },
            onDelete = { viewModel.eliminarTarea(it) },
            onEdit = { tarea ->
                tareaParaEdicion = tarea
                nuevoTextoEdicion = tarea.titulo
            }
        )
    }

    tareaParaEdicion?.let { tareaInfo ->
        AlertDialog(
            onDismissRequest = { tareaParaEdicion = null },
            title = { Text("Modificar Tarea") },
            text = {
                OutlinedTextField(
                    value = nuevoTextoEdicion,
                    onValueChange = { nuevoTextoEdicion = it },
                    label = { Text("Nuevo nombre") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (nuevoTextoEdicion.isNotBlank()) {
                        viewModel.EdicionTarea(tareaInfo, nuevoTextoEdicion)
                        tareaParaEdicion = null
                    }
                }) {
                    Text("Actualizar")
                }
            },
            dismissButton = {
                TextButton(onClick = { tareaParaEdicion = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun TituloApp() {
    Text(
        text = "Gestor de Tareas",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun BotonPrimario(texto: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(texto)
    }
}

@Composable
fun CampoTexto(valor: String, onValorChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ListaTareas(
    tareas: List<Tarea>,
    onToggle: (Tarea) -> Unit,
    onDelete: (Tarea) -> Unit,
    onEdit: (Tarea) -> Unit
) {
    LazyColumn {
        items(tareas) { tarea ->
            ItemTarea(
                tarea = tarea,
                onToggle = { onToggle(tarea) },
                onDelete = { onDelete(tarea) },
                onEdit = { onEdit(tarea) }
            )
        }
    }
}

@Composable
fun ItemTarea(tarea: Tarea, onToggle: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
    TarjetaBase {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Checkbox(checked = tarea.completada, onCheckedChange = { onToggle() })
                Text(
                    text = tarea.titulo,
                    modifier = Modifier.padding(start = 8.dp),
                    color = if (tarea.completada) Color.Gray else Color.Black
                )
            }
            Row {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
            }
        }
    }
}

@Composable
fun TarjetaBase(contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        contenido()
    }
}