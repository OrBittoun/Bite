package com.example.first_app_version.data.remote.model

data class CategoryResponse(
    val categories: List<CategoryDto>
)

//Data transfer object
data class CategoryDto(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)
