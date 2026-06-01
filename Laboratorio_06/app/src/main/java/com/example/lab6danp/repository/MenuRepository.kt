package com.example.lab6danp.repository

import com.example.lab6danp.R
import com.example.lab6danp.model.Coffee
import com.example.lab6danp.model.Dessert
import com.example.lab6danp.model.MenuItem

interface MenuRepository {
    fun getMenuItems(): List<MenuItem>
    fun getItemById(id: Int): MenuItem? // Nuevo método para la vista de detalles
}

class LocalMenuRepositoryImpl : MenuRepository {
    private val items = listOf(
        Coffee(1, "Espresso Intenso", 5.50, "Un shot de energía pura y aroma profundo.", R.drawable.espresso, true),
        Coffee(2, "Cappuccino", 9.00, "Espresso con leche vaporizada y mucha espuma.", R.drawable.cappuccino, true),
        Coffee(3, "Frappé Caramelo", 14.50, "Café helado licuado con crema batida y caramelo.", R.drawable.frappe, false),
        Dessert(4, "Croissant de Mantequilla", 7.00, "Masa hojaldrada, dorada y crujiente.", R.drawable.croissant, false),
        Dessert(5, "Muffin de Arándanos", 8.50, "Esponjoso, hecho con harina integral.", R.drawable.muffin, true)
    )

    override fun getMenuItems() = items
    override fun getItemById(id: Int) = items.find { it.id == id }
}