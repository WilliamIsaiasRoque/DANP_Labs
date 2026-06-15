package com.example.lab8danp.presentation.viewmodel

import com.example.lab8danp.domain.model.VideoCard

sealed class VideoCardUiState {
    // Estado inicial y durante las peticiones
    object Loading : VideoCardUiState()

    // Estado cuando los datos se recuperan correctamente
    data class Success(val videoCards: List<VideoCard>) : VideoCardUiState()

    // Estado en caso de que falle el repositorio
    data class Error(val message: String) : VideoCardUiState()
}