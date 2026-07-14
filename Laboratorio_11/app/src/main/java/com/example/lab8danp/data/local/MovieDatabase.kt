package com.example.lab8danp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab8danp.data.local.dao.MovieDao
import com.example.lab8danp.data.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 2, // v2: se agregó la columna hasNotification (push notifications FCM)
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}