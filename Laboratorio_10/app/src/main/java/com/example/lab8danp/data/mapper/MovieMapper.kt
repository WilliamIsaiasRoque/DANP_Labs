package com.example.lab8danp.data.mapper

import com.example.lab8danp.data.local.entity.MovieEntity
import com.example.lab8danp.data.remote.dto.MovieDto
import com.example.lab8danp.domain.model.Movie

// Convierte de la base de datos local al dominio (Para la UI)
fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview
    )
}

// Convierte del DTO (API) a la base de datos local (Room)
fun MovieDto.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        updatedAt = System.currentTimeMillis()
    )
}