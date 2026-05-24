package com.example.lab5danp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.lab5danp.ui.theme.AppThemeMode

@Composable
fun ThemeSelector(
    onThemeSelected: (AppThemeMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AppButton(text = "Blue", onClick = { onThemeSelected(AppThemeMode.BLUE) })
        AppButton(text = "Green", onClick = { onThemeSelected(AppThemeMode.GREEN) })
        AppButton(text = "Purple", onClick = { onThemeSelected(AppThemeMode.PURPLE) })
    }
}