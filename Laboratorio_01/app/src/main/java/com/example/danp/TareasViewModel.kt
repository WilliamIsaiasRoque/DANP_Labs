package com.example.danp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Entity(tableName = "tabla_tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val completada: Boolean = false
)

@Dao
interface TareaDao {
    @Query("SELECT * FROM tabla_tareas")
    fun obtenerTodas(): Flow<List<Tarea>>

    @Insert
    suspend fun insertarTarea(tarea: Tarea)

    @Update
    suspend fun actualizarTarea(tarea: Tarea)

    @Delete
    suspend fun eliminarTarea(tarea: Tarea)
}

@Database(entities = [Tarea::class], version = 1, exportSchema = false)
abstract class TareaDatabase : RoomDatabase() {
    abstract fun tareaDao(): TareaDao

    companion object {
        @Volatile
        private var INSTANCE: TareaDatabase? = null

        fun getDatabase(context: Context): TareaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TareaDatabase::class.java,
                    "tarea_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
enum class TipoFiltro { NINGUNO, SOLO_PENDIENTES, SOLO_COMPLETADAS }
class TareasViewModel(private val tareaDao: TareaDao) : ViewModel() {


    private val _filtroActivo = MutableStateFlow(TipoFiltro.NINGUNO)
    val filtroActivo: StateFlow<TipoFiltro> = _filtroActivo


    val tareasFiltradas: StateFlow<List<Tarea>> = tareaDao.obtenerTodas()
        .combine(_filtroActivo) { listaTareas, filtro ->
            when (filtro) {
                TipoFiltro.NINGUNO -> listaTareas
                TipoFiltro.SOLO_PENDIENTES -> listaTareas.filter { !it.completada }
                TipoFiltro.SOLO_COMPLETADAS -> listaTareas.filter { it.completada }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun cambiarFiltro(nuevoFiltro: TipoFiltro) {
        _filtroActivo.value = nuevoFiltro
    }

    fun agregarTarea(nombre: String) {
        viewModelScope.launch { tareaDao.insertarTarea(Tarea(titulo = nombre)) }
    }

    fun toggleTarea(tarea: Tarea) {
        viewModelScope.launch { tareaDao.actualizarTarea(tarea.copy(completada = !tarea.completada)) }
    }

    fun eliminarTarea(tarea: Tarea) {
        viewModelScope.launch { tareaDao.eliminarTarea(tarea) }
    }

    fun EdicionTarea(tarea: Tarea, nuevoTitulo: String) {
        viewModelScope.launch { tareaDao.actualizarTarea(tarea.copy(titulo = nuevoTitulo)) }
    }
}

class TareasViewModelFactory(private val dao: TareaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TareasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TareasViewModel(dao) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}