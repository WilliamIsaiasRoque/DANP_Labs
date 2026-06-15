package com.example.lab8danp.data.repository

import com.example.lab8danp.domain.model.VideoCard
import com.example.lab8danp.domain.repository.VideoCardRepository
import javax.inject.Inject

class VideoCardRepositoryRealImpl @Inject constructor() : VideoCardRepository {

    // Por ahora, retorna una lista vacía para cumplir con el contrato de la interfaz.
    override fun getVideoCards(): List<VideoCard> {
        return emptyList()
    }

    override fun getVideoCardById(id: Int): VideoCard? {
        return null
    }
}