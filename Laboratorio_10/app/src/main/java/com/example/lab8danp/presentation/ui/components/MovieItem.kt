package com.example.lab8danp.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
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
fun MovieItem(movie: Movie) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
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

            Text(
                text = movie.title,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}