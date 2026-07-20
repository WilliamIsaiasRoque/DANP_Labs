package com.example.lab8danp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lab8danp.presentation.viewmodel.MovieUiState
import com.example.lab8danp.presentation.viewmodel.MovieViewModel
import com.example.lab8danp.presentation.ui.components.GenreFilterBar
import com.example.lab8danp.presentation.ui.components.MovieItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(viewModel: MovieViewModel, onMovieClick: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Catálogo de Películas", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = uiState) {
                is MovieUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is MovieUiState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Barra de filtros por género (solo con géneros que tienen contenido)
                        GenreFilterBar(
                            genres = state.genres,
                            selectedGenreId = state.selectedGenreId,
                            onGenreSelected = viewModel::onGenreSelected
                        )

                        HorizontalDivider()

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.movies, key = { it.id }) { movie ->
                                MovieItem(movie = movie, onClick = { onMovieClick(movie.id) })
                            }
                        }
                    }
                }
                is MovieUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
