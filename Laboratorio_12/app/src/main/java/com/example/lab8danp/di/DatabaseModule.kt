package com.example.lab8danp.di

import android.content.Context
import androidx.room.Room
import com.example.lab8danp.data.local.MovieDatabase
import com.example.lab8danp.data.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        // "movies_db" será el nombre del archivo físico en el celular
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movies_db"
        )
            // No manejamos migraciones formales en este proyecto de práctica
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }
}