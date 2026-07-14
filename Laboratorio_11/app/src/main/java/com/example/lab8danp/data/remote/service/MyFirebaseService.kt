package com.example.lab8danp.data.remote.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.lab8danp.MainActivity
import com.example.lab8danp.R
import com.example.lab8danp.core.Constants
import com.example.lab8danp.domain.repository.MovieRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Procesa exclusivamente Data Messages: la app construye la notificación localmente
 * (no se usa el bloque "notification" del payload de FCM).
 */
@AndroidEntryPoint
class MyFirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var repository: MovieRepository

    // Scope propio del Service (Hilt no inyecta un viewModelScope aquí porque no es un ViewModel)
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: enviar el token al backend propio para asociarlo al usuario/dispositivo.
        Log.d("MyFirebaseService", "Nuevo token FCM: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Solo procesamos Data Messages (remoteMessage.notification se ignora a propósito)
        val data = remoteMessage.data
        val movieId = data["movieId"]?.toIntOrNull() ?: return
        val title = data["title"] ?: "Película destacada"
        val body = data["body"] ?: "Toca para ver el detalle"

        // 1) Delegamos al Repository: actualiza Room -> el StateFlow del ViewModel refresca la UI solo
        serviceScope.launch {
            repository.markMovieAsNotified(movieId)
        }

        // 2) Construimos y mostramos la notificación local
        showNotification(movieId, title, body)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun showNotification(movieId: Int, title: String, body: String) {
        createNotificationChannel()

        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(Constants.buildMovieDeepLink(movieId)),
            this,
            MainActivity::class.java
        )

        val pendingIntent = PendingIntent.getActivity(
            this,
            movieId, // requestCode único por película para no pisar PendingIntents anteriores
            deepLinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return // Sin permiso (Android 13+), no podemos notificar
        }

        NotificationManagerCompat.from(this).notify(movieId, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alertas de nuevas películas destacadas"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}
