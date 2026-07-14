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

    // FIX: Ahora devuelve List<Long> (los IDs insertados) para evitar el error "V" (Void)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>): List<Long>

    // FIX: Ahora devuelve Int (las filas borradas) para evitar el error "V" (Void)
    @Query("DELETE FROM movies")
    suspend fun clearMovies(): Int
}