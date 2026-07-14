package com.example.lab8danp.domain.repository

import com.example.lab8danp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // Devuelve un flujo reactivo directamente desde la base de datos
    fun getMovies(): Flow<List<Movie>>

    // Devuelve una película puntual de forma reactiva (para la pantalla de detalle)
    fun getMovieById(movieId: Int): Flow<Movie?>

    // Fuerza la descarga de nuevos datos desde la API
    suspend fun syncMovies()

    // Llamado por MyFirebaseService al recibir una push notification (Data Message)
    suspend fun markMovieAsNotified(movieId: Int)
}