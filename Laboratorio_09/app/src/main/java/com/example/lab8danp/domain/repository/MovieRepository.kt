package com.example.lab8danp.domain.repository

import com.example.lab8danp.domain.model.Movie

interface MovieRepository {
    suspend fun getMovies(): List<Movie>
}