package com.example.lab8danp.presentation.viewmodel

import com.example.lab8danp.domain.model.Genre
import com.example.lab8danp.domain.model.Movie

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(
        val movies: List<Movie>,          // Ya filtradas según el género seleccionado
        val genres: List<Genre> = emptyList(), // Solo los géneros que tienen películas
        val selectedGenreId: Int? = null       // null = "Todas"
    ) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}
