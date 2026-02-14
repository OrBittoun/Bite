package com.example.first_app_version.data.remote.model

data class CategoryResponse(
    val categories: List<CategoryDto>
)

data class CategoryDto( //Data transfer object
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)
