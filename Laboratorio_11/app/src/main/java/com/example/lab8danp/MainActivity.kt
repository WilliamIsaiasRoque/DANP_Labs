package com.example.lab8danp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.lab8danp.presentation.navigation.AppNavGraph
import com.example.lab8danp.presentation.ui.theme.Lab8danpTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab8danpTheme {
                // El NavHost registra el deep link que abre MyFirebaseService al tocar la notificación
                AppNavGraph()
            }
        }
    }
}