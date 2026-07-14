package com.example.lab8danp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab8danp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    init {
        observeDatabase()
        syncNetworkData()
    }

    private fun observeDatabase() {
        viewModelScope.launch {
            repository.getMovies().collect { movies ->
                if (movies.isEmpty()) {
                    _uiState.value = MovieUiState.Loading
                } else {
                    _uiState.value = MovieUiState.Success(movies)
                }
            }
        }
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