package com.example.lab8danp.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.lab8danp.domain.repository.MovieRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MovieRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Llama a la función que descarga de la API y guarda en Room
            repository.syncMovies()
            Result.success()
        } catch (e: Exception) {
            // Si falla (ej. el servidor de TMDB se cae), le decimos que reintente luego
            Result.retry()
        }
    }
}