package com.example.lab6danp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lab6danp.model.MenuItem
import com.example.lab6danp.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel(private val repository: MenuRepository) : ViewModel() {
    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems

    init { loadMenu() }

    private fun loadMenu() {
        _menuItems.value = repository.getMenuItems()
    }

    // El ViewModel se encarga de la lógica de búsqueda (SRP)
    fun getItem(id: Int): MenuItem? {
        return repository.getItemById(id)
    }
}