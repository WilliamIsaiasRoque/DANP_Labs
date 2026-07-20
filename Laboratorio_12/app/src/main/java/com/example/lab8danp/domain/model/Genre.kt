package com.example.lab8danp.domain.model

data class Genre(
    val id: Int,
    val name: String,
    val movieCount: Int
)

// Catálogo oficial de géneros de TMDB (estable, evita una llamada extra a la API)
object Genres {
    val NAMES: Map<Int, String> = mapOf(
        28 to "Acción",
        12 to "Aventura",
        16 to "Animación",
        35 to "Comedia",
        80 to "Crimen",
        99 to "Documental",
        18 to "Drama",
        10751 to "Familia",
        14 to "Fantasía",
        36 to "Historia",
        27 to "Terror",
        10402 to "Música",
        9648 to "Misterio",
        10749 to "Romance",
        878 to "Ciencia ficción",
        10770 to "Película de TV",
        53 to "Suspense",
        10752 to "Bélica",
        37 to "Western"
    )

    // Un género solo se muestra como filtro si tiene al menos esta cantidad de películas
    const val MIN_MOVIES_TO_SHOW = 5
}
