package com.example.lab7danp.model

data class Product(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val description: String,
    val imageRes: Int,
    val category: String
)