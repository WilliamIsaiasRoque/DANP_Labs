package com.example.lab8danp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lab8danp.presentation.ui.screens.CatalogScreen
import com.example.lab8danp.presentation.ui.theme.Lab8danpTheme
import dagger.hilt.android.AndroidEntryPoint

// Habilita a Hilt para inyectar dependencias en este Activity y sus fragmentos/composables hijos
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab8danpTheme {
                CatalogScreen()
            }
        }
    }
}