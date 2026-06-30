package com.example.lab8danp.presentation.viewmodel

import com.example.lab8danp.domain.model.Movie

sealed class MovieUiState {
    object Loading : MovieUiState()
    data class Success(val movies: List<Movie>) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}