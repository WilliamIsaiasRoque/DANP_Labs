package com.example.lab8danp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// El nombre de la tabla será "movies"
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val overview: String,
    val updatedAt: Long = System.currentTimeMillis(), // Para saber cuándo se guardó (timestamp)
    val hasNotification: Boolean = false // Marcada por una push notification (FCM)
)