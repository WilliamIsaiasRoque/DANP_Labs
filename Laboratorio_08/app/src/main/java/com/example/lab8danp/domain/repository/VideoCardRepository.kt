package com.example.lab8danp.domain.repository

import com.example.lab8danp.domain.model.VideoCard

interface VideoCardRepository {
    fun getVideoCards(): List<VideoCard>
    fun getVideoCardById(id: Int): VideoCard?
}