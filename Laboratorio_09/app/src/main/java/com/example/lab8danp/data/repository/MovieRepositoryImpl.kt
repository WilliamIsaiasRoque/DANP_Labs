package com.example.lab8danp.data.repository

import com.example.lab8danp.core.Constants
import com.example.lab8danp.data.mapper.toDomain
import com.example.lab8danp.data.remote.TMDBApi
import com.example.lab8danp.domain.model.Movie
import com.example.lab8danp.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TMDBApi
) : MovieRepository {
    override suspend fun getMovies(): List<Movie> {
        // Solicitamos las dos primeras páginas de la API
        val responsePage1 = api.getPopularMovies(apiKey = Constants.API_KEY, page = 1).results
        val responsePage2 = api.getPopularMovies(apiKey = Constants.API_KEY, page = 2).results

        // Fusionamos ambas listas y las convertimos al modelo de dominio
        return (responsePage1 + responsePage2).map { it.toDomain() }
    }
}