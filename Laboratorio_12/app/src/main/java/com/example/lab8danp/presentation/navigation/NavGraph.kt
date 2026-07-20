package com.example.lab8danp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.lab8danp.core.Constants
import com.example.lab8danp.presentation.ui.screens.MovieDetailScreen
import com.example.lab8danp.presentation.ui.screens.MovieScreen
import com.example.lab8danp.presentation.viewmodel.MovieDetailViewModel
import com.example.lab8danp.presentation.viewmodel.MovieViewModel

object Routes {
    const val MOVIE_LIST = "movies"
    const val MOVIE_DETAIL = "movieDetail/{movieId}"

    fun movieDetail(movieId: Int) = "movieDetail/$movieId"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.MOVIE_LIST) {
        composable(Routes.MOVIE_LIST) {
            val viewModel: MovieViewModel = hiltViewModel()
            MovieScreen(
                viewModel = viewModel,
                onMovieClick = { movieId ->
                    navController.navigate(Routes.movieDetail(movieId))
                }
            )
        }

        composable(
            route = Routes.MOVIE_DETAIL,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType }),
            // Mismo patrón de URI que usa MyFirebaseService para el PendingIntent de la notificación
            deepLinks = listOf(navDeepLink { uriPattern = Constants.DEEP_LINK_MOVIE_URI })
        ) {
            val viewModel: MovieDetailViewModel = hiltViewModel()
            MovieDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
