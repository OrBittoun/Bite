package com.example.first_app_version.data.models

data class DishPreview(
    val dishId: Int,
    val categoryName: String? = null,
    val imageRes: Int? = null,
    val imageUrl: String? = null
)
