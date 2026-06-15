package com.example.lab8danp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab8danp.domain.model.VideoCard
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

    // Caché en memoria para evitar llamadas redundantes al buscar
    private var allCards: List<VideoCard> = emptyList()

    init {
        loadVideoCards()
    }

    private fun loadVideoCards() {
        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(1000)
                allCards = getVideoCardsUseCase()
                _uiState.value = VideoCardUiState.Success(allCards)
            } catch (e: Exception) {
                _uiState.value = VideoCardUiState.Error("Error al cargar datos: ${e.message}")
            }
        }
    }

    // Lógica de filtrado inyectada en la UI reactiva
    fun searchVideoCards(query: String) {
        if (allCards.isNotEmpty()) {
            val filteredList = allCards.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.brand.contains(query, ignoreCase = true)
            }
            _uiState.value = VideoCardUiState.Success(filteredList)
        }
    }
}