package com.example.lab8danp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab8danp.data.local.dao.MovieDao
import com.example.lab8danp.data.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}