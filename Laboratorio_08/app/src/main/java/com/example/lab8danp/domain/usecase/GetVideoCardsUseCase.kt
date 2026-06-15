package com.example.lab8danp.domain.usecase

import com.example.lab8danp.domain.model.VideoCard
import com.example.lab8danp.domain.repository.VideoCardRepository
import javax.inject.Inject

class GetVideoCardsUseCase @Inject constructor(
    private val repository: VideoCardRepository
) {
    // El operador invoke permite llamar a la clase como si fuera una función
    operator fun invoke(): List<VideoCard> {
        // Aquí iría la lógica de negocio extra (ordenar alfabéticamente o filtrar por ej)
        return repository.getVideoCards().sortedBy { it.price }
    }
}