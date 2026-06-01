package com.example.lab6danp.model

interface MenuItem {
    val id: Int
    val name: String
    val price: Double
    val description: String
    val imageRes: Int // Nueva propiedad para la foto
}

interface Heatable { val isHot: Boolean }
interface VeganFriendly { val isVegan: Boolean }

data class Coffee(
    override val id: Int, override val name: String, override val price: Double,
    override val description: String, override val imageRes: Int, override val isHot: Boolean
) : MenuItem, Heatable

data class Dessert(
    override val id: Int, override val name: String, override val price: Double,
    override val description: String, override val imageRes: Int, override val isVegan: Boolean
) : MenuItem, VeganFriendly