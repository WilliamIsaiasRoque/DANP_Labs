package com.example.lab8danp.data.remote.dto
import com.google.gson.annotations.SerializedName

data class MovieResponseDto(val results: List<MovieDto>)

data class MovieDto(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    val overview: String,
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    val popularity: Double = 0.0
)
