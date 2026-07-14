package com.example.lab8danp.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lab8danp.core.Constants
import com.example.lab8danp.domain.model.Movie

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = "${Constants.IMAGE_BASE_URL}${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Row(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (movie.hasNotification) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificación nueva",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}