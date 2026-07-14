package com.example.lab8danp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab8danp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY updatedAt DESC")
    fun getMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :movieId LIMIT 1")
    fun getMovieById(movieId: Int): Flow<MovieEntity?>

    // Marca la película como notificada y la sube al tope de la lista (Room notifica al Flow automáticamente)
    // Devuelve Int (filas actualizadas) para evitar el bug de KSP con retorno Void/Unit implícito
    @Query("UPDATE movies SET hasNotification = 1, updatedAt = :timestamp WHERE id = :movieId")
    suspend fun markAsNotified(movieId: Int, timestamp: Long = System.currentTimeMillis()): Int

    // FIX: Ahora devuelve List<Long> (los IDs insertados) para evitar el error "V" (Void)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>): List<Long>

    // FIX: Ahora devuelve Int (las filas borradas) para evitar el error "V" (Void)
    @Query("DELETE FROM movies")
    suspend fun clearMovies(): Int
}