package com.example.danp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.flow.Flow
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

class TareasViewModel(private val tareaDao: TareaDao) : ViewModel() {
    val todasLasTareas: Flow<List<Tarea>> = tareaDao.obtenerTodas()
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