package com.example.lab8danp.data.remote

import com.example.lab8danp.data.remote.dto.MovieResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") lang: String = "es-PE",
        @Query("page") page: Int
    ): MovieResponseDto
}