package com.example.first_app_version.ui.all_kitchens

data class DishPreview(
    val dishId: Int,
    val categoryName: String = "",
    val imageRes: Any //image or URL
)
