package com.example.danplab04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class User(val id: Int, val name: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntiPatternScreen()
        }
    }
}

@Composable
fun AntiPatternScreen() {
    var users by remember {
        mutableStateOf(List(1000) { User(it, "User $it") })
    }
    Column {
        Button(onClick = {
            users = users + User(users.size, "Nuevo Usuario")
        }) {
            Text("Agregar usuario")
        }
        LazyColumn {
            items(users) { user ->
                Text(
                    text = user.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(16.dp)
                )
            }
        }
    }
}