package com.example.lab8danp.data.repository

import com.example.lab8danp.core.Constants
import com.example.lab8danp.core.NetworkMonitor
import com.example.lab8danp.data.local.dao.MovieDao
import com.example.lab8danp.data.mapper.toDomain
import com.example.lab8danp.data.mapper.toEntity
import com.example.lab8danp.data.remote.TMDBApi
import com.example.lab8danp.domain.model.Movie
import com.example.lab8danp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    private val dao: MovieDao,
    private val networkMonitor: NetworkMonitor
) : MovieRepository {

    // 1. La UI solo lee de aquí. Cuando Room cambia, este Flow avisa a la UI.
    override fun getMovies(): Flow<List<Movie>> {
        return dao.getMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMovieById(movieId: Int): Flow<Movie?> {
        return dao.getMovieById(movieId).map { it?.toDomain() }
    }

    // Actualiza Room al llegar una push notification. El Flow de getMovies()/getMovieById()
    // emite automáticamente, y el ViewModel (StateFlow) refresca la UI sin que el Service la toque.
    override suspend fun markMovieAsNotified(movieId: Int) {
        dao.markAsNotified(movieId)
    }

    // 2. Esta función se conecta a internet y actualiza la base de datos silenciosamente.
    override suspend fun syncMovies() {
        if (!networkMonitor.isConnected()) return // Si no hay internet, no hace nada

        try {
            // Bajamos las dos páginas
            val page1 = api.getPopularMovies(Constants.API_KEY, page = 1).results
            val page2 = api.getPopularMovies(Constants.API_KEY, page = 2).results
            val allMovies = page1 + page2

            // Guardamos en Room. Al hacer esto, el 'getMovies()' reacciona solo.
            dao.insertMovies(allMovies.map { it.toEntity() })
        } catch (e: Exception) {
            e.printStackTrace() // Si falla la red de golpe, lo ignoramos porque ya hay datos locales
        }
    }
}