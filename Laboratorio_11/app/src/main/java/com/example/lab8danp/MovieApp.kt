package com.example.lab8danp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.lab8danp.data.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MovieApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    // Le decimos a WorkManager que use la fábrica de Hilt
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        setupWorkManager()
    }

    private fun setupWorkManager() {
        // Regla: Solo sincronizar si hay conexión a internet
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Tarea periódica: Cada 15 minutos
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        // Encola la tarea para que Android la gestione en segundo plano
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SyncMoviesWork",
            ExistingPeriodicWorkPolicy.KEEP, // Si ya está programada, la mantiene
            syncWorkRequest
        )
    }
}