package com.example.danp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = TareaDatabase.getDatabase(applicationContext)
        val viewModel: TareasViewModel by viewModels { TareasViewModelFactory(db.tareaDao()) }

        setContent {
            val sistemaEnOscuro = isSystemInDarkTheme()
            var modoOscuroActivo by remember { mutableStateOf(sistemaEnOscuro) }
            val misColores = if (modoOscuroActivo) darkColorScheme() else lightColorScheme()

            MaterialTheme(colorScheme = misColores) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppTareas(
                        viewModel = viewModel,
                        esModoOscuro = modoOscuroActivo,
                        onCambiarTema = { modoOscuroActivo = it }
                    )
                }
            }
        }
    }
}

@Composable
fun AppTareas(
    viewModel: TareasViewModel,
    esModoOscuro: Boolean,
    onCambiarTema: (Boolean) -> Unit
) {
    val tareas by viewModel.tareasFiltradas.collectAsState()
    val filtroActual by viewModel.filtroActivo.collectAsState()

    var textoInput by remember { mutableStateOf("") }
    var tareaParaEdicion by remember { mutableStateOf<Tarea?>(null) }
    var nuevoTextoEdicion by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TituloApp()
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Modo Oscuro", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = esModoOscuro,
                    onCheckedChange = onCambiarTema
                )
            }
        }

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

        FilaDeFiltros(
            filtroActual = filtroActual,
            onFiltroSeleccionado = { viewModel.cambiarFiltro(it) }
        )

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
fun FilaDeFiltros(filtroActual: TipoFiltro, onFiltroSeleccionado: (TipoFiltro) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val filtros = listOf(
            "Todas" to TipoFiltro.NINGUNO,
            "Pendientes" to TipoFiltro.SOLO_PENDIENTES,
            "Completadas" to TipoFiltro.SOLO_COMPLETADAS
        )

        filtros.forEach { (nombre, tipo) ->
            val estaActivo = filtroActual == tipo
            Button(
                onClick = { onFiltroSeleccionado(tipo) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (estaActivo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (estaActivo) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                Text(nombre, style = MaterialTheme.typography.labelSmall)
            }
        }
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Checkbox(
                    checked = tarea.completada,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.tertiary
                    )
                )

                val textColor by animateColorAsState(
                    targetValue = if (tarea.completada)
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.onSurface,
                    label = "textColorAnimation"
                )

                Text(
                    text = tarea.titulo,
                    modifier = Modifier.padding(start = 8.dp),
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        contenido()
    }
}