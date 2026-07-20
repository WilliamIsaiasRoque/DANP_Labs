package com.example.lab8danp.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val overview: String,
    val hasNotification: Boolean = false,
    val genreIds: List<Int> = emptyList(),
    val popularity: Double = 0.0
)
