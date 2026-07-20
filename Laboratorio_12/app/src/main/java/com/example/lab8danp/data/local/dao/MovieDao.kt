package com.example.lab8danp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab8danp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // Las notificadas primero, luego por popularidad (ranking real de TMDB)
    @Query("SELECT * FROM movies ORDER BY hasNotification DESC, popularity DESC")
    fun getMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :movieId LIMIT 1")
    fun getMovieById(movieId: Int): Flow<MovieEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>): List<Long>

    @Query("DELETE FROM movies")
    suspend fun clearMovies(): Int

    // Marca la película como notificada (Room notifica al Flow automáticamente)
    // Devuelve Int (filas actualizadas) para evitar el bug de KSP con retorno Void/Unit implícito
    @Query("UPDATE movies SET hasNotification = 1 WHERE id = :movieId")
    suspend fun markAsNotified(movieId: Int): Int

    // Permite conservar las marcas de notificación cuando el sync reescribe las filas
    @Query("SELECT id FROM movies WHERE hasNotification = 1")
    suspend fun getNotifiedIds(): List<Int>
}
