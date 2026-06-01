package com.example.lab6danp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CoffeeColorScheme = lightColorScheme(
    primary = CoffeeBrown,
    secondary = Caramel,
    tertiary = CoffeeLight,
    background = CreamBackground,
    surface = CreamBackground,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.Black
)

@Composable
fun Lab6danpTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CoffeeColorScheme,
        typography = Typography, // Asumiendo que tu archivo Type.kt está por defecto
        content = content
    )
}