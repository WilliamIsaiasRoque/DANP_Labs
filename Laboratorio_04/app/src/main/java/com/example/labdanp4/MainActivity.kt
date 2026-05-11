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
            SnapshotStateListScreen()
        }
    }
}

@Composable
fun SnapshotStateListScreen() {
    val users = remember {
        mutableStateListOf<User>().apply {
            addAll(List(1000) { User(it, "User $it") })
        }
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
                UserItem(user)
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Text(user.name)
        if (expanded) {
            Text("Detalles del usuario ${user.id}")
        }
    }
}