package com.example.first_app_version.ui.all_kitchens

data class HomeCategory(
    val kitchenId: Int,
    val kitchenName: String,
    val previewDishIds: List<Int>
)