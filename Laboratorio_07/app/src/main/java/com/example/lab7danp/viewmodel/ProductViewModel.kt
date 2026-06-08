package com.example.lab7danp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab7danp.model.Product
import com.example.lab7danp.model.ProductUiState
import com.example.lab7danp.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Cumple Requisito 6 y 7: MVVM y Patrón UI State
class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    // Elevación de Estado (State Hoisting) centralizado
    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _uiState.value = ProductUiState.Loading
        viewModelScope.launch {
            val products = repository.getProducts()
            _uiState.value = ProductUiState.Success(products)
        }
    }

    // Cumple Requisito 3: Búsqueda por nombre o categoría
    fun searchProducts(query: String) {
        val results = repository.searchProducts(query)
        _uiState.value = ProductUiState.Success(results)
    }

    // Cumple Requisito 1: Operaciones CRUD
    fun addProduct(product: Product) {
        repository.addProduct(product)
        loadProducts() // Recomposición automática de la lista
    }

    fun updateProduct(product: Product) {
        repository.updateProduct(product)
        loadProducts()
    }

    fun deleteProduct(id: Int) {
        repository.deleteProduct(id)
        loadProducts()
    }

    fun getProductById(id: Int): Product? {
        return repository.getProductById(id)
    }
}