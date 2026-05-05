package com.example.danplab2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

// 1. DataStore para preferencias simples (Requerimiento 1)
val Context.dataStore by preferencesDataStore(name = "settings")
val USER_NAME_KEY = stringPreferencesKey("user_name")

// --- NAVEGACIÓN Y RUTAS (Requerimiento 6) ---
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details/{habitId}") {
        fun createRoute(habitId: Int) = "details/$habitId"
    }
}

// --- PERSISTENCIA ROOM (Requerimiento 1 y 2) ---
@Entity(tableName = "habits_table")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isCompletedToday: Boolean = false,
    val streak: Int = 0,
    val completedDates: List<String> = emptyList() // Historial de dias
)

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String = value?.joinToString(",") ?: ""
    @TypeConverter
    fun toStringList(value: String?): List<String> = if (value.isNullOrBlank()) emptyList() else value.split(",")
}

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits_table")
    fun getAllHabits(): Flow<List<Habit>>
    @Query("SELECT * FROM habits_table WHERE id = :id")
    fun getHabitById(id: Int): Flow<Habit?>
    @Insert suspend fun insertHabit(habit: Habit)
    @Update suspend fun updateHabit(habit: Habit)
    @Delete suspend fun deleteHabit(habit: Habit)
}

@Database(entities = [Habit::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    companion object {
        @Volatile private var INSTANCE: HabitDatabase? = null
        fun getDatabase(context: Context): HabitDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, HabitDatabase::class.java, "habit_db").build().also { INSTANCE = it }
            }
        }
    }
}

// --- VIEWMODEL: Lógica de Rachas y Filtros (Requerimiento 3 y 4) ---
class HabitViewModel(private val dao: HabitDao, private val context: Context) : ViewModel() {
    private val _currentFilter = MutableStateFlow(HabitFilter.ALL)
    val currentFilter: StateFlow<HabitFilter> = _currentFilter

    val filteredHabits: StateFlow<List<Habit>> = dao.getAllHabits()
        .combine(_currentFilter) { habits, filter ->
            when (filter) {
                HabitFilter.ALL -> habits
                HabitFilter.COMPLETED -> habits.filter { it.isCompletedToday }
                HabitFilter.PENDING -> habits.filter { !it.isCompletedToday }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userName: Flow<String> = context.dataStore.data.map { it[USER_NAME_KEY] ?: "Usuario" }

    fun saveName(name: String) {
        viewModelScope.launch { context.dataStore.edit { it[USER_NAME_KEY] = name } }
    }

    fun getHabit(id: Int): Flow<Habit?> = dao.getHabitById(id)
    fun setFilter(filter: HabitFilter) { _currentFilter.value = filter }
    fun addHabit(title: String) { viewModelScope.launch { dao.insertHabit(Habit(title = title)) } }
    fun deleteHabit(habit: Habit) { viewModelScope.launch { dao.deleteHabit(habit) } }

    fun toggleHabit(habit: Habit, isCompleted: Boolean) {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            val updatedDates = if (isCompleted) {
                if (!habit.completedDates.contains(today)) habit.completedDates + today else habit.completedDates
            } else {
                habit.completedDates.filter { it != today }
            }
            dao.updateHabit(habit.copy(isCompletedToday = isCompleted, completedDates = updatedDates, streak = calculateStreak(updatedDates)))
        }
    }

    private fun calculateStreak(dates: List<String>): Int {
        if (dates.isEmpty()) return 0
        val sortedDates = dates.map { LocalDate.parse(it) }.sortedDescending()
        var streak = 0
        var checkDate = LocalDate.now()
        if (!dates.contains(checkDate.toString())) checkDate = checkDate.minusDays(1)
        for (date in sortedDates) {
            if (date == checkDate) { streak++; checkDate = checkDate.minusDays(1) }
            else if (date.isBefore(checkDate)) break
        }
        return streak
    }
}

enum class HabitFilter { ALL, COMPLETED, PENDING }

class HabitViewModelFactory(private val dao: HabitDao, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return HabitViewModel(dao, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// --- ACTIVITY ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = HabitDatabase.getDatabase(applicationContext)
        val viewModel: HabitViewModel by viewModels { HabitViewModelFactory(db.habitDao(), applicationContext) }

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) { HabitListScreen(navController, viewModel) }
                    // Requerimiento 7: Paso de parámetros
                    composable(Screen.Details.route) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("habitId")?.toIntOrNull()
                        HabitDetailScreen(id, navController, viewModel)
                    }
                }
            }
        }
    }
}

// --- UI: PANTALLA PRINCIPAL ---
@Composable
fun HabitListScreen(navController: NavController, viewModel: HabitViewModel) {
    var inputHabit by remember { mutableStateOf("") }
    var inputName by remember { mutableStateOf("") }
    val habits by viewModel.filteredHabits.collectAsState()
    val filter by viewModel.currentFilter.collectAsState()
    val name by viewModel.userName.collectAsState(initial = "")
    val progress = if (habits.isNotEmpty()) habits.count { it.isCompletedToday }.toFloat() / habits.size else 0f

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Hola, $name", style = MaterialTheme.typography.headlineMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(value = inputName, onValueChange = { inputName = it }, placeholder = { Text("Nombre") }, modifier = Modifier.weight(1f))
            Button(onClick = { viewModel.saveName(inputName); inputName = "" }, modifier = Modifier.padding(start = 8.dp)) { Text("Save") }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HabitFilter.entries.forEach { f ->
                FilterChip(selected = filter == f, onClick = { viewModel.setFilter(f) }, label = { Text(f.name) })
            }
        }

        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        Row {
            TextField(value = inputHabit, onValueChange = { inputHabit = it }, modifier = Modifier.weight(1f), placeholder = { Text("Nuevo hábito") })
            Button(onClick = { if (inputHabit.isNotBlank()) { viewModel.addHabit(inputHabit); inputHabit = "" } }, modifier = Modifier.padding(start = 8.dp)) { Text("Ok") }
        }

        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(habits, key = { it.id }) { habit ->
                HabitItem(habit, { viewModel.toggleHabit(habit, it) }, { viewModel.deleteHabit(habit) }, { navController.navigate(Screen.Details.createRoute(habit.id)) })
            }
        }
    }
}

@Composable
fun HabitItem(habit: Habit, onToggle: (Boolean) -> Unit, onDelete: () -> Unit, onDetailClick: () -> Unit) {
    // Requerimiento 5: Cambio de color al marcar
    val color by animateColorAsState(if (habit.isCompletedToday) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant, label = "")

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onDetailClick() }, colors = CardDefaults.cardColors(containerColor = color)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = habit.isCompletedToday, onCheckedChange = onToggle)
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(text = habit.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Racha: ${habit.streak} 🔥", color = Color(0xFFE65100), style = MaterialTheme.typography.labelMedium)
            }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null) }
        }
    }
}

// --- UI: PANTALLA DETALLES (Requerimiento 10) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(habitId: Int?, navController: NavController, viewModel: HabitViewModel) {
    val habit by if (habitId != null) viewModel.getHabit(habitId).collectAsState(initial = null) else remember { mutableStateOf(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas") },
                navigationIcon = {
                    // Requerimiento 10: Botón de regreso (popBackStack)
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            habit?.let { h ->
                Text(text = h.title, style = MaterialTheme.typography.headlineSmall)
                Text(text = "Racha actual: ${h.streak} días", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Historial:", style = MaterialTheme.typography.titleMedium)
                LazyColumn {
                    items(h.completedDates) { date ->
                        Text(text = "✓ Completado el $date", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            } ?: Text("Cargando...")
        }
    }
}