package com.example.lab8danp.data.mapper
import com.example.lab8danp.data.remote.dto.MovieDto
import com.example.lab8danp.domain.model.Movie

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview
    )
}