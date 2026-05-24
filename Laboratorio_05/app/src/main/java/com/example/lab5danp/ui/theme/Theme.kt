package com.example.lab5danp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

enum class AppThemeMode {
    BLUE, GREEN, PURPLE
}

@Composable
fun ModularStoreTheme(
    themeMode: AppThemeMode,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.BLUE -> lightColorScheme(primary = BluePrimary)
        AppThemeMode.GREEN -> lightColorScheme(primary = GreenPrimary)
        AppThemeMode.PURPLE -> lightColorScheme(primary = PurplePrimary)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}