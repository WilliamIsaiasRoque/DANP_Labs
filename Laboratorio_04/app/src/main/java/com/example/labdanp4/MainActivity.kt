package com.example.labdanp4

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
            FinalOptimizedScreen()
        }
    }
}

@Composable
fun FinalOptimizedScreen() {
    val users = remember {
        mutableStateListOf<User>().apply {
            addAll(List(1000) { User(it, "User $it") })
        }
    }

    val itemModifier = remember {
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    }

    Column {
        Button(onClick = {
            users.add(User(users.size, "Nuevo Usuario"))
        }) {
            Text("Agregar usuario")
        }

        LazyColumn {
            items(
                items = users,
                key = { it.id }
            ) { user ->
                UserItemOptimized(user, itemModifier)
            }
        }
    }
}

@Composable
fun UserItemOptimized(user: User, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {
        Text(user.name)
        if (expanded) {
            Text("Detalles del usuario ${user.id}")
        }
    }
}