package com.example.lab8danp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lab8danp.presentation.ui.screens.MovieScreen
import com.example.lab8danp.presentation.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Hilt provee el ViewModel automáticamente con todas sus dependencias
            val viewModel: MovieViewModel = hiltViewModel()
            MovieScreen(viewModel = viewModel)
        }
    }
}