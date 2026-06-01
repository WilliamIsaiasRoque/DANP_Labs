package com.example.lab6danp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.lab6danp.navigation.AppNavigation
import com.example.lab6danp.repository.LocalMenuRepositoryImpl
import com.example.lab6danp.ui.theme.Lab6danpTheme
import com.example.lab6danp.viewmodel.MenuViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inyeccion de dependencias
        val repository = LocalMenuRepositoryImpl()
        val viewModel = MenuViewModel(repository)

        setContent {
            Lab6danpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Delegamos la UI al NavHost, pasándole el ViewModel inyectado
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}