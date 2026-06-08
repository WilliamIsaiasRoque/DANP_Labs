package com.example.lab7danp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.lab7danp.navigation.AppNavigation
import com.example.lab7danp.repository.FakeProductRepositoryImpl
import com.example.lab7danp.ui.theme.Lab7danpTheme
import com.example.lab7danp.viewmodel.ProductViewModel
import com.example.lab7danp.viewmodel.ProductViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Instanciamos el Repos
        val repository = FakeProductRepositoryImpl()

        //Usamos el ViewModelFactory
        val factory = ProductViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        setContent {
            Lab7danpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}