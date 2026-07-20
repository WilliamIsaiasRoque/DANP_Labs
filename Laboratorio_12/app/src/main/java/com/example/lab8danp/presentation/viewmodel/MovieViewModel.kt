package com.example.lab8danp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab8danp.domain.model.Genre
import com.example.lab8danp.domain.model.Genres
import com.example.lab8danp.domain.model.Movie
import com.example.lab8danp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    // Género seleccionado en la barra de filtros (null = "Todas")
    private val _selectedGenreId = MutableStateFlow<Int?>(null)

    init {
        observeDatabase()
        syncNetworkData()
    }

    fun onGenreSelected(genreId: Int?) {
        _selectedGenreId.value = genreId
    }

    private fun observeDatabase() {
        viewModelScope.launch {
            // Combinamos las películas de Room con el filtro elegido:
            // cada vez que cambie cualquiera de los dos, se recalcula la pantalla.
            combine(repository.getMovies(), _selectedGenreId) { movies, genreId ->
                movies to genreId
            }.collect { (movies, genreId) ->
                if (movies.isEmpty()) {
                    _uiState.value = MovieUiState.Loading
                    return@collect
                }

                val genres = buildAvailableGenres(movies)

                // Si el género elegido dejó de existir, volvemos a "Todas"
                val validGenreId = genreId?.takeIf { id -> genres.any { it.id == id } }

                val filtered = if (validGenreId == null) {
                    movies
                } else {
                    movies.filter { validGenreId in it.genreIds }
                }

                _uiState.value = MovieUiState.Success(
                    movies = filtered,
                    genres = genres,
                    selectedGenreId = validGenreId
                )
            }
        }
    }

    // Solo mostramos los géneros que realmente tienen películas descargadas.
    // Así nunca aparece un filtro que al tocarlo salga vacío.
    private fun buildAvailableGenres(movies: List<Movie>): List<Genre> {
        return movies
            .flatMap { it.genreIds }
            .groupingBy { it }
            .eachCount()
            .filter { (id, count) ->
                count >= Genres.MIN_MOVIES_TO_SHOW && Genres.NAMES.containsKey(id)
            }
            .map { (id, count) -> Genre(id = id, name = Genres.NAMES.getValue(id), movieCount = count) }
            .sortedByDescending { it.movieCount }
    }

    private fun syncNetworkData() {
        viewModelScope.launch {
            try {
                repository.syncMovies()
            } catch (e: Exception) {
                // Solo mostramos error si la base de datos local está completamente vacía y no hay red
                if (_uiState.value is MovieUiState.Loading) {
                    _uiState.value = MovieUiState.Error("Sin conexión a internet y sin datos locales.")
                }
            }
        }
    }
}
