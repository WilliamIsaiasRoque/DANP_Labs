package com.example.lab8danp.domain.repository

import com.example.lab8danp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // Devuelve un flujo reactivo directamente desde la base de datos
    fun getMovies(): Flow<List<Movie>>

    // Fuerza la descarga de nuevos datos desde la API
    suspend fun syncMovies()
}