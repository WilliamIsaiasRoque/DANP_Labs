package com.example.lab7danp.repository

import com.example.lab7danp.model.Product

interface ProductRepository {
    fun getProducts(): List<Product>
    fun getProductById(id: Int): Product?
    fun addProduct(product: Product)
    fun updateProduct(product: Product)
    fun deleteProduct(id: Int)
    fun searchProducts(query: String): List<Product>
}

class FakeProductRepositoryImpl : ProductRepository {

    private val productsList = mutableListOf(
        Product(1, "Laptop ASUS ROG", 4500.0, "Laptop gamer 16GB RAM, RTX 3060", android.R.drawable.ic_menu_camera, "Electrónica"),
        Product(2, "Mouse Logitech G502", 250.0, "Mouse gamer inalámbrico", android.R.drawable.ic_menu_camera, "Accesorios"),
        Product(3, "Silla Ergohuman", 1200.0, "Silla de oficina ergonómica", android.R.drawable.ic_menu_camera, "Oficina")
    )

    override fun getProducts(): List<Product> = productsList.toList()

    override fun getProductById(id: Int): Product? = productsList.find { it.id == id }

    override fun addProduct(product: Product) {
        val newId = (productsList.maxOfOrNull { it.id } ?: 0) + 1
        productsList.add(product.copy(id = newId))
    }

    override fun updateProduct(product: Product) {
        val index = productsList.indexOfFirst { it.id == product.id }
        if (index != -1) {
            productsList[index] = product
        }
    }

    override fun deleteProduct(id: Int) {
        productsList.removeAll { it.id == id }
    }

    override fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return getProducts()
        return productsList.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
    }
}