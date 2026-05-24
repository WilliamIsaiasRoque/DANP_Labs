package com.example.lab5danp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.lab5danp.navigation.AppNavigation
import com.example.lab5danp.ui.theme.AppThemeMode
import com.example.lab5danp.ui.theme.ModularStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentTheme by remember { mutableStateOf(AppThemeMode.BLUE) }

            ModularStoreTheme(themeMode = currentTheme) {
                AppNavigation {
                    currentTheme = when (it) {
                        "GREEN" -> AppThemeMode.GREEN
                        "PURPLE" -> AppThemeMode.PURPLE
                        else -> AppThemeMode.BLUE
                    }
                }
            }
        }
    }
}