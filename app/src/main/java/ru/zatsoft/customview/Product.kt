package ru.zatsoft.customview

import java.io.Serializable

data class Product(val name: String, val price: Double, var image: String, val description: String): Serializable
