package com.example.lab8danp.core

object Constants {
    const val API_KEY = "c16e7da7bafc18ec7c2f89c2f5277dce"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    // FCM / Notificaciones
    const val NOTIFICATION_CHANNEL_ID = "movies_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Notificaciones de Películas"

    // Deep Link (usado por Navigation Compose y por el PendingIntent del FCM)
    const val DEEP_LINK_SCHEME = "lab8danp"
    const val DEEP_LINK_HOST = "movie"
    const val DEEP_LINK_MOVIE_URI = "$DEEP_LINK_SCHEME://$DEEP_LINK_HOST/{movieId}"

    fun buildMovieDeepLink(movieId: Int) = "$DEEP_LINK_SCHEME://$DEEP_LINK_HOST/$movieId"
}