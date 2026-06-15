package com.example.lab8danp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab8danp.domain.usecase.GetVideoCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoCardViewModel @Inject constructor(
    private val getVideoCardsUseCase: GetVideoCardsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<VideoCardUiState>(VideoCardUiState.Loading)
    val uiState: StateFlow<VideoCardUiState> = _uiState.asStateFlow()

    init {
        loadVideoCards()
    }

    private fun loadVideoCards() {
        viewModelScope.launch {
            try {
                // Simulamos un retraso de 1 segundo para apreciar el UI State de Loading
                kotlinx.coroutines.delay(1000)

                val cards = getVideoCardsUseCase()
                _uiState.value = VideoCardUiState.Success(cards)
            } catch (e: Exception) {
                _uiState.value = VideoCardUiState.Error("Error al cargar datos: ${e.message}")
            }
        }
    }
}